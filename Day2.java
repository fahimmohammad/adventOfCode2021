import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Scanner;

public class Day2 {
    public static void main(String[] args){
       Scanner scan = new Scanner(System.in);
       int depth=0;
       int forward=0;
       int currentStep = 0;
       int currentString;
       int aim = 0;
       while(scan.hasNext()){
           currentString = scan.next().trim().length();
           if(currentString==1)
               break;
           currentStep = Integer.parseInt(scan.next());
           if(currentString==7){
               forward+=currentStep;
               depth+=currentStep*aim;
           }
           else if(currentString==4)
               aim+=currentStep;
           else
               aim-= currentStep;
       }
       scan.close();
       System.out.println(depth);
       System.out.println(forward);
       System.out.println(depth*forward);
    }
}
