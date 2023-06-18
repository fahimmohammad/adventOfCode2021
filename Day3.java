import java.util.ArrayList;
import java.util.Scanner;

public class Day3 {

    public static void main(String[] args){
        Scanner scan = new Scanner(System.in);
        int[] arr1 = new int[12];
        int[] arr0 = new int[12];
        int first = 0;
        int second = 0;
        String row;
        int i=0;
        while (scan.hasNext()){
            row = scan.next().trim();
            if(row.length()==1)
                break;
            i=0;
            while(i<12){
                if(row.charAt(i) == '1')
                    arr1[i]=arr1[i]+1;
                else
                    arr0[i]=arr0[i]+1;
                i++;
            }
        }
        scan.close();
        for(i=0;i<12;i++){
            if(arr1[i]>arr0[i]){
                arr1[i] = 1;
                arr0[i] = 0;
            }else{
                arr1[i] = 0;
                arr0[i] = 1;
            }
            int n= 12-i;
            int p = 1;
            while (n-->1){
                p= p*2;
            }
            System.out.println("P="+p);
            first+=arr1[i]*p;
            second+= arr0[i]*p;
        }
        System.out.println(first+" "+second+" >>"+first*second);
    }
}
/*
Answer=3847100

 */
