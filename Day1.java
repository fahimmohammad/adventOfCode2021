import java.util.*;

public class Day1 {
    public static void main(String[] args){
        Scanner scan = new Scanner(System.in);
        int increased = 0;
        int f1 =scan.nextInt();
        int f2 = scan.nextInt();
        int f3 = scan.nextInt();
        int sum1 = 0;
        int sum2=0;
        sum1 = f1+f2+f3;
        while(scan.hasNextInt()){
            f1=f2;
            f2=f3;
            f3=scan.nextInt();
            sum2 = f1+f2+f3;
            if(sum2>sum1)
                increased++;
            sum1=sum2;
        }
        System.out.println(increased);
        scan.close();
    }
}
