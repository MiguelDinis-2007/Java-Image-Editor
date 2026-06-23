// Projeto de IP - Miguel Ferreira Alves Dinis - nº138808
import pt.iscte.greyditor.Greyditor;
import pt.iscte.greyditor.Editor;
import pt.iscte.greyditor.Selection;
void main() {
    Greyditor configuration = new Greyditor("ProjetoIP");
    configuration.addFilter("Invert", this::invert);
    configuration.addFilter("Darken/Brighten", this::DarkenBrighten, -255, 255);
    configuration.addFilter("Contrast", this::Contrast, 0, 255);
    configuration.addFilter("Grain", this::Grain,0,255);
    configuration.addEffect("Grid", this::grid);
    configuration.addEffect("Lines", this::lines, 0, 50);
    configuration.addEffect("Vignette (Darken/Brighten)", this::Vignette, -255, 255);
    configuration.addEffect("Horizontal Mirror", this::HorizontalMirror);
    configuration.addEffect("Vertical Mirror", this::VerticalMirror);
    configuration.addEffect("Margin", this::Margin);
    configuration.addOperation("Square", this::square);
    configuration.addOperation("Darken Area", this::darkenArea);
    configuration.addOperation("Crop", this::Crop);
    configuration.addOperation("Expand", this::Expand);
    configuration.addOperation("Posterize", this::Posterize);
    configuration.addOperation("Rotate", this::Rotate);
    configuration.addOperation("Blur", this::GetBlurRadius);
    configuration.addOperation("Ancient Filter", this::Ancient);
    configuration.addOperation("Retro Filter", this::Retro);
    configuration.addOperation("Copy", this::Copy);
    configuration.addOperation("Cut", this::Cut);
    configuration.addOperation("Paste", this::Paste);
    configuration.addOperation("Undo", this::Undo);
    configuration.addLoadOperation("Load");
    configuration.addSaveOperation("Save");
    configuration.open("monalisa.jpg");
}
int[][] beforeImage; // Variável para ser usada no Undo
int[][] copiedImage; // Variável para ser usada no Copy
boolean isCopied = false; // Variável para determinar se é Copy
boolean isCut = false; // Variável para determinar se é Cut
int cutTimes = 0; // Variável para determinar as vezes que o utilizador fez Cut
boolean action = false; // Variável que determina se algo já foi feito (para o Undo)
void save(int[][] image){ // Função de guardar
    beforeImage = new int[image.length][image[0].length]; // Variável que guarda
    for(int y = 0; y<image.length; y++){
        for(int x = 0; x<image[y].length; x++){
            beforeImage[y][x]=image[y][x]; // Iteração para igual cada pixel à imagem dada
        }
    }
    action=true; // Determinar que algo já foi feito
}
int invert(int tone) { // Função que inverte o tom de cada pixel
    return 255 - tone;
}
int DarkenBrighten(int tone, int intensity) { // Função que escurece ou clareia cada pixel
    if(intensity>=0) {
        return Math.min(255, tone+intensity);
    }else{
        return Math.max(0, tone+intensity);
    }
}
int Contrast(int tone, int intensity){ // Função que torna os pixeis claros mais claros e os pixeis escuros mais escuros
    if(tone>255/2) {
        return Math.min(255, tone+intensity);
    }else{
        return Math.max(0, tone-intensity);
    }
}
void grid(int[][] image) { // Função que desenha uma Grid sobre a imagem
    int hspace = (image[0].length + 2) / 3;
    int vspace = (image.length + 2) / 3;
    for (int y = vspace; y < image.length; y += vspace)
        for (int x = 0; x < image[y].length; x++)
            image[y][x] = 200;
    for (int x = hspace; x < image[0].length; x += hspace)
        for (int y = 0; y < image.length; y++)
            image[y][x] = 200;
}
void lines(int[][] image, int spacing) { // Função que desenha linhas sobre a imagem
    if (spacing == 0)
        return;
    for (int y = 0; y < image.length; y += spacing)
        for (int x = 0; x < image[y].length; x++)
            image[y][x] = 0;
}
int[][] square(int[][] image) { // Função que transforma a imagem num quadrado
    save(image);
    int side = Math.min(image.length, image[0].length);
    int[][] square = new int[side][side];
    for (int y = 0; y < side; y++)
        for (int x = 0; x < side; x++)
            square[y][x] = image[y][x];

    return square;
}
int[][] darkenArea(int[][] image, Editor editor) { // Função que escurece uma parte da imagem selecionada
    Selection selection = editor.getSelection();
    if (selection == null) {
        editor.message("Please select an area of the image.");
    } else {
        if(selection.height()!=-1||selection.width()!=-1) {
            save(image);
            int factor = editor.getInteger("Intensity?");
            for (int y = selection.y(); y < selection.y() + selection.height(); y++)
                for (int x = selection.x(); x < selection.x() + selection.width(); x++)
                    image[y][x] = Math.max(0, image[y][x] - factor);
        }else{
            editor.message("You must select more than 1 pixel.");
        }
    }
    return null;
}
void HorizontalMirror(int[][] image){ // Função que espelha a imagem horizontalmente
    for(int y = 0; y<image.length/2; y++){
        int invert = image.length-1-y;
        int[] temp = image[y];
        image[y] = image[invert];
        image[invert] = temp;
    }
}
void VerticalMirror(int[][] image) { // Função que espelha a imagem verticalmente
    for (int y = 0; y < image.length; y++) {
        for (int x = 0; x < image[y].length / 2; x++) {
            int invert = image[y].length - 1 - x;
            int temp = image[y][x];
            image[y][x] = image[y][invert];
            image[y][invert] = temp;
        }
    }
}
int Grain(int tone, int intensity) { // Função que clareia (sensivelmente) metado dos pixeis e escurece a outra metade
    if(Math.random()>.5) {
        return Math.max(0,tone-intensity);
    }
    return Math.min(255, tone+intensity);
}
int[][] Margin(int[][] image){ // Função que cria uma margem sobre a imagem
    for(int y = 0; y<image.length; y++){
        for(int x = 0; x<image[y].length; x++){
            if(x<28||y<28) {
                image[y][image[y].length - x - 1] = 255;
                image[image.length - y - 1][x] = 255;
                image[y][x] = 255;
            }
        }
    }
    return image;
}
int[][] Vignette(int[][] image, int intensity) { // Função que cria uma vinheta (clara ou escura) sobre a imagem
    int width = image[0].length;
    int height = image.length;
    int centerX = width / 2;
    int centerY = height / 2;
    double maxDistance = Math.sqrt(centerX * centerX + centerY * centerY);
    for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
            double distance = Math.sqrt(Math.pow(x - centerX, 2) + Math.pow(y - centerY, 2));
            double normalized = distance / maxDistance;
            int darkening = (int)(intensity * normalized);
            int newTone = image[y][x] - darkening;
            image[y][x] = Math.max(0, Math.min(255, newTone));
        }
    }
    return image;
}
int[][] Crop(int[][] image, Editor editor){ // Função que aproxima para uma parte selecionada da imagem
    Selection selection = editor.getSelection();
    int[][] crop = new int[0][0];
    if (selection == null) {
        editor.message("Please select an area of the image.");
    } else {
        if(selection.height()!=-1||selection.width()!=-1) {
            save(image);
            crop = new int[selection.height()][selection.width()];
            for (int y = selection.y(); y < selection.y() + selection.height(); y++) {
                for (int x = selection.x(); x < selection.x() + selection.width(); x++) {
                    crop[y - selection.y()][x - selection.x()] = image[y][x];
                }
            }
        }else{
            editor.message("You must select more than 1 pixel.");
        }
        return crop;
    }
    return null;
}
int[][] Expand (int[][] image, Editor editor) { // Função que expande a imagem
    int altura = 0;
    int largura = 0;
    while((altura<=image.length || largura <=image[0].length) || (altura>1000 || largura>1000)){
        altura = editor.getInteger("Height?"); // variavél da altura
        largura = editor.getInteger("With?"); // variável da largura
        if(altura<=image.length || largura <=image[0].length){
            editor.message("The expansion must be bigger than the original image.");
        }
        if(altura>1000 || largura>1000){
            editor.message("The expansion must be shorter than 1000x1000.");
        }
    }
    int[][] expandedImage = new int[altura][largura]; // criação da imagem expandida
    save(image);
    for(int y = 0; y<image.length; y++){
        for(int x = 0; x<image[y].length; x++){
            expandedImage[(altura-image.length)/2+y][(largura-image[y].length)/2+x]=image[y][x]; //Expansão da Imagem
        }
    }
    return expandedImage;
}
int[][] Posterize(int[][] image, Editor editor){ // Função que determina um certo número de cores para a imagem
    int colors = 0;
    while(colors<=1||colors>20) {
        colors = editor.getInteger("Colors?");
        if (colors <= 1) {
            editor.message("The color must be more than 1.");
        }
        if (colors > 20) {
            editor.message("The color must be less than 20.");
        }
    }
    int[][] posterizedImage = new int[image.length][image[0].length];
    save(image);
    for(int y = 0; y<image.length; y++){
        for(int x = 0; x<image[0].length; x++){
            for (int temp = 1; temp <= colors; temp++) {
                if(image[y][x]<=(255/colors)*temp && image[y][x]>=(255/colors)*(temp-1)){
                    posterizedImage[y][x]=(255/colors-1)*temp-1;
                }
            }
        }
    }
    return posterizedImage;
}
int[][] Rotate (int[][] image){ // Função que roda a imagem a 90º graus para a direita
    int[][] rotatedImage = new int[image[0].length][image.length];
    save(image);
    for(int y = 0; y<image.length; y++){
        for(int x = 0; x<image[y].length; x++){
            if(image.length>image[0].length) {
                rotatedImage[x][y] = image[image.length - y - 1][image[y].length - x - 1];
            }else{
                rotatedImage[x][y] = image[y][x];
            }
        }
    }
    return rotatedImage;
}
int[][] Blur(int[][] image, int r){ // Função que provoca um blur à imagem com um raio
    int[][] blurredImage = new int[image.length][image[0].length];
    save(image);
    for(int y = 0; y<image.length; y++){
        for(int x = 0; x<image[0].length; x++){
            int sum = 0;
            int pix = 0;
            for (int i = y-r; i <= y+r ; i++) {
                for (int j = x-r; j <= x+r; j++) {
                    if ((i >= 0 && i < image.length) && (j >= 0 && j < image[0].length)) {
                        sum += image[i][j];
                        pix++;
                    }
                }
            }
            blurredImage[y][x]=sum/pix;
        }
    }
    return blurredImage;
}
int[][] GetBlurRadius(int[][] image, Editor editor){ // Função para adquirir o raio para o Blur
    int r = 0;
    while(r<1||r>10) {
        r = editor.getInteger("Radius?");
        if (r < 1) {
            editor.message("The radius must be bigger than 1.");
        }
        if (r > 10) {
            editor.message("The radius must be smaller than 10.");
        }
    }
    return Blur(image, r);
}
int[][] Ancient(int[][] image){ // Função para o filtro Ancient que utiliza o Grain, o Margin e a Vignette
    save(image);
    for(int y = 0; y<image.length; y++){
        for(int x = 0; x<image[y].length; x++){
            image[y][x]=Grain(image[y][x],20);
        }
    }
    return Margin(Vignette(image,150));
}
int[][] Retro(int[][] image){ // Função para o filtro Retro que utiliza o Contrast, o Blur e a Vignette
    save(image);
    for(int y = 0; y<image.length; y++){
        for(int x = 0; x<image[y].length; x++){
            image[y][x]=Contrast(image[y][x],70);
        }
    }
    return Blur(Vignette(image, 150),3);
}
int[][] Copy(int[][] image, Editor editor){ // Função que copia uma parte da imagem selecionada
    Selection selection = editor.getSelection();
    if (selection == null) {
        editor.message("Please select an area of the image.");
    } else {
        if (selection.height() != -1 || selection.width() != -1) {
            copiedImage = new int[selection.height()][selection.width()];
            isCopied = true;
            isCut = false;
            cutTimes = 0;
            for (int y = selection.y(); y < selection.y() + selection.height(); y++) {
                for (int x = selection.x(); x < selection.x() + selection.width(); x++) {
                    copiedImage[y - selection.y()][x - selection.x()] = image[y][x];
                }
            }
        }else{
            editor.message("You must select more than 1 pixel.");
        }
    }
    return null;
}
int[][] Cut(int[][] image, Editor editor){ // Função que apaga e copia uma parte da imagem selecionada
    Selection selection = editor.getSelection();
    if (selection == null) {
        editor.message("Please select an area of the image.");
    } else {
        if(selection.width()!=-1||selection.height()!=-1) {
            save(image);
            copiedImage = new int[selection.height()][selection.width()];
            isCopied = false;
            isCut = true;
            cutTimes = 0;
            for (int y = selection.y(); y < selection.y() + selection.height(); y++) {
                for (int x = selection.x(); x < selection.x() + selection.width(); x++) {
                    copiedImage[y - selection.y()][x - selection.x()] = image[y][x];
                    image[y][x] = 255;
                }
            }
        }else{
            editor.message("You must select more than 1 pixel.");
        }
        return image;
    }
    return null;
}
int[][] Paste(int[][] image, Editor editor) { // Função que cola uma parte da imagem copiada (com copy ou cut)
    Selection selection = editor.getSelection();
    if (selection == null) {
        editor.message("Please select an area of the image.");
    } else {
        if (selection.width() != -1 || selection.height() != -1) {
            editor.message("You must only select one pixel.");
        } else {
            if (isCopied) {
                save(image);
                for (int y = selection.y(); y < image.length; y++) {
                    if (y - selection.y() == copiedImage.length) {
                        y = image.length;
                    } else {
                        for (int x = selection.x(); x < image[0].length; x++) {
                            if (x - selection.x() == copiedImage[0].length) {
                                x = image[0].length;
                            } else {
                                image[y][x] = copiedImage[y - selection.y()][x - selection.x()];
                            }
                        }
                    }
                }
            } else {
                if (isCut) {
                    if (cutTimes < 1) {
                        save(image);
                        cutTimes++;
                        for (int y = selection.y(); y < image.length; y++) {
                            if (y - selection.y() == copiedImage.length) {
                                y = image.length;
                            } else {
                                for (int x = selection.x(); x < image[0].length; x++) {
                                    if (x - selection.x() == copiedImage[0].length) {
                                        x = image[0].length;
                                    } else {
                                        image[y][x] = copiedImage[y - selection.y()][x - selection.x()];
                                    }
                                }
                            }
                        }
                    }else{
                        editor.message("You can only cut and copy once.");
                    }
                } else {
                    editor.message("You must first copy/cut something.");
                }
            }
        }
    }
    return image;
}
int[][] Undo(int[][] image, Editor editor){ // Função que volta atrás (apenas uma vez)
    if(beforeImage!=null){
        if(!action) {
            editor.message("Software in progress, you can only undo once, please wait patiently for more updates :)");
        }else{
            action=false;
            return beforeImage;
        }
    }else{
        editor.message("You must do something before you can undo.");
    }
    return null;
}