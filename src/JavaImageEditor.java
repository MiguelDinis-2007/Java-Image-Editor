import pt.iscte.greyditor.Greyditor;
import pt.iscte.greyditor.Editor;
import pt.iscte.greyditor.Selection;

void main() {
    Greyditor configuration = new Greyditor("JavaImageEditor");
    configuration.addFilter("Invert", this::invert);
    configuration.addFilter("Darken/Brighten", this::darkenBrighten, -255, 255);
    configuration.addFilter("Contrast", this::contrast, 0, 255);
    configuration.addFilter("Grain", this::grain,0,255);
    configuration.addEffect("Grid", this::grid);
    configuration.addEffect("Lines", this::lines, 0, 50);
    configuration.addEffect("Vignette (Darken/Brighten)", this::vignette, -255, 255);
    configuration.addEffect("Horizontal Mirror", this::horizontalMirror);
    configuration.addEffect("Vertical Mirror", this::verticalMirror);
    configuration.addEffect("Margin", this::margin);
    configuration.addOperation("Square", this::square);
    configuration.addOperation("Darken Area", this::darkenArea);
    configuration.addOperation("Crop", this::crop);
    configuration.addOperation("Expand", this::expand);
    configuration.addOperation("Posterize", this::posterize);
    configuration.addOperation("Rotate", this::rotate);
    configuration.addOperation("Blur", this::getBlurRadius);
    configuration.addOperation("Ancient Filter", this::ancientFilter);
    configuration.addOperation("Retro Filter", this::retroFilter);
    configuration.addOperation("Copy", this::copy);
    configuration.addOperation("Cut", this::cut);
    configuration.addOperation("Paste", this::paste);
    configuration.addOperation("Undo", this::undo);
    configuration.addLoadOperation("Load");
    configuration.addSaveOperation("Save");
    configuration.open("monalisa.jpg");
}

int[][] beforeImage; // variable to be used in undo

int[][] copiedImage; // variable to be used in copy

boolean isCopied = false; // variable to determine if it's copied

boolean isCut = false; // variable to determine if it's cut

int cutTimes = 0; // variable to determine how many times the user has used cut

boolean action = false; // variable to determine if something has happened (for the cut function)

void save(int[][] image){ // save function to save an image before an alteration
    beforeImage = new int[image.length][image[0].length];
    for(int y = 0; y<image.length; y++){
        for(int x = 0; x<image[y].length; x++){
            beforeImage[y][x]=image[y][x];
        }
    }
    action=true;
}

int invert(int tone) { // Função que inverte o tom de cada pixel
    return 255 - tone;
} // invert function that replaces the tone of each pixel with (255-tone)

int darkenBrighten(int tone, int intensity) { // function that darkens/brightens every pixel
    if(intensity>=0) {
        return Math.min(255, tone+intensity);
    }else{
        return Math.max(0, tone+intensity);
    }
}

int contrast(int tone, int intensity){ // function that highlights the darkness and lightness of the image
    if(tone>255/2) {
        return Math.min(255, tone+intensity);
    }else{
        return Math.max(0, tone-intensity);
    }
}

void grid(int[][] image) { // function that draws a grid over the image
    int hspace = (image[0].length + 2) / 3;
    int vspace = (image.length + 2) / 3;
    for (int y = vspace; y < image.length; y += vspace)
        for (int x = 0; x < image[y].length; x++)
            image[y][x] = 200;
    for (int x = hspace; x < image[0].length; x += hspace)
        for (int y = 0; y < image.length; y++)
            image[y][x] = 200;
}

void lines(int[][] image, int spacing) { // function that draws lines over the image
    if (spacing == 0)
        return;
    for (int y = 0; y < image.length; y += spacing)
        for (int x = 0; x < image[y].length; x++)
            image[y][x] = 0;
}

int[][] square(int[][] image) { // function that sets the resolution to a square form
    save(image);
    int side = Math.min(image.length, image[0].length);
    int[][] square = new int[side][side];
    for (int y = 0; y < side; y++)
        for (int x = 0; x < side; x++)
            square[y][x] = image[y][x];

    return square;
}

int[][] darkenArea(int[][] image, Editor editor) { // function that darkens the selected part of the image
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

void horizontalMirror(int[][] image){ // function that mirrors the image horizontal
    for(int y = 0; y<image.length/2; y++){
        int invert = image.length-1-y;
        int[] temp = image[y];
        image[y] = image[invert];
        image[invert] = temp;
    }
}

void verticalMirror(int[][] image) { // function that mirrors the image vertical
    for (int y = 0; y < image.length; y++) {
        for (int x = 0; x < image[y].length / 2; x++) {
            int invert = image[y].length - 1 - x;
            int temp = image[y][x];
            image[y][x] = image[y][invert];
            image[y][invert] = temp;
        }
    }
}

int grain(int tone, int intensity) { // function that brightens (around) half and darkens the other half of the image
    if(Math.random()>.5) {
        return Math.max(0,tone-intensity);
    }
    return Math.min(255, tone+intensity);
}

int[][] margin(int[][] image){ // function that creates a margin over the image
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

int[][] vignette(int[][] image, int intensity) { // function that creates a vignette over the image
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

int[][] crop(int[][] image, Editor editor){ // function that crops into the selected part of the image
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

int[][] expand (int[][] image, Editor editor) { // function that alters the resolution of the image to a bigger one
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
    int[][] expandedImage = new int[altura][largura];
    save(image);
    for(int y = 0; y<image.length; y++){
        for(int x = 0; x<image[y].length; x++){
            expandedImage[(altura-image.length)/2+y][(largura-image[y].length)/2+x]=image[y][x]; //Expansão da Imagem
        }
    }
    return expandedImage;
}

int[][] posterize(int[][] image, Editor editor){ // function that determines the number of tones in the image
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

int[][] rotate (int[][] image){ // function that rotates the image 90º
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

int[][] blur(int[][] image, int r){ // function that turns the image into a blurred image with a radius
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

int[][] getBlurRadius(int[][] image, Editor editor){ // function to get the radius to blur
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
    return blur(image, r);
}

int[][] ancientFilter(int[][] image){ // function for the ancient filter that uses grain, margin and vignette
    save(image);
    for(int y = 0; y<image.length; y++){
        for(int x = 0; x<image[y].length; x++){
            image[y][x]=grain(image[y][x],20);
        }
    }
    return margin(vignette(image,150));
}

int[][] retroFilter(int[][] image){ // function for the retro filter that uses contrast, blur and vignette
    save(image);
    for(int y = 0; y<image.length; y++){
        for(int x = 0; x<image[y].length; x++){
            image[y][x]=contrast(image[y][x],70);
        }
    }
    return blur(vignette(image, 150),3);
}

int[][] copy(int[][] image, Editor editor){ // function that copies the selected part of the image
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

int[][] cut(int[][] image, Editor editor){ // function that erases and copies the selected part of the image
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

int[][] paste(int[][] image, Editor editor) { // function that pastes a copied part of the image (through copy/cut)
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

int[][] undo(int[][] image, Editor editor){ // function that undoes only the previous alteration
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