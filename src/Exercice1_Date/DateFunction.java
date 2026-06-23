public class Date{
    // Variables
    private int day;
    private int month;
    private int year;

    // Constructor
    public Date (int month , int day , int year){
        this.day = day;
        this.month = month;
        this.year = year;
    }

    // Day / Month / Year
    public int day(){
        return this.day;
    }
    public int month(){
        return this.month;
    }
    public int year(){
        return this.year;
    }

    // Date in String (M/D/Y)
    public String toString(){
        return month() + "/" + day() + "/" + year();
    }

    // Check if "other" Date is before this Date
    public boolean before(Date other){
        return (other.year()>this.year())||(other.year()==this.year() && other.month()>this.month()) || (other.month()==this.month() && other.day()>this.day());
    }

    // Check if year is leap year
    private boolean isLeapYear(int year){
        return (year%4==0&&(year%100!=0 || year%400==0));
    }
    // Check the days since the beginning of the year
    public int daysSinceBeginYear(){
        int days = 0;
        for(int i = 1; i<this.month();i++){
            if(i%2==0){
                if(i==2){
                    if(isLeapYear(this.year())){
                        days+=29;
                    }else{
                        days+=28;
                    }
                }else{
                    if(i<=7) {
                        days+=30;
                    }else{
                        days+=31;
                    }
                }
            }else{
                if(i<=7) {
                    days+=31;
                }else{
                    days+=30;
                }
            }
        }
        return days+this.day();
    }

    // Check the days until the end of the year
    public int daysUntilEndYear(){
        if(isLeapYear(this.year())){
            return 366-daysSinceBeginYear();
        }else{
            return 365-daysSinceBeginYear();
        }
    }

    // Check the days between 2 dates
    public int daysBetween(Date other){
        if(this.before(other)){
            if (this.year() == other.year()) {
                return other.daysSinceBeginYear()-this.daysSinceBeginYear();
            }else if(other.year()-this.year()==1){
                return this.daysUntilEndYear()+other.daysSinceBeginYear();
            }else{
                int daysOfYearsBetween = 0;
                for(int i = this.year()+1; i<other.year()-this.year(); i++){
                    if(isLeapYear(i)){
                        daysOfYearsBetween+=366;
                    }else{
                        daysOfYearsBetween+=365;
                    }
                }
                return this.daysUntilEndYear()+other.daysSinceBeginYear()+daysOfYearsBetween;
            }
        }
        return 0;
    }
}

void main(){
    Date date = new Date(2, 26, 2025);
    Date other = new Date(2,25,2027);
    IO.println(other.daysBetween(date));
}