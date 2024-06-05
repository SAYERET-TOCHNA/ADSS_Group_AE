package org.example.Utilities;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

// this class was created to wrap Scanner & handle the
// fcking exceptions that it throws every time you try to read something
// from the console.  like I get it you throw an exception but why throw
// my program into a death loop?  I just want to read a number from the console
// take a 0.5 second break and then read another input smh...

// I'm not mad at you Scanner, I'm just disappointed.
// I'm sorry I yelled at you, I didn't mean it.  I just want to read a number
// I swear to god github copilot wrote the last 2 lines lmao

public class SmartReader {


    public SmartReader() {
    }

    public int readInt(){
        int input = 0;
        while(true)
        {
            try{
                TimeUnit.MILLISECONDS.sleep(500);
                Scanner scanner = new Scanner(System.in);
                input = scanner.nextInt();
                return input;
            } catch (Exception e){
                System.out.print("error / Invalid input, please enter another number: ");
                continue;
            }
        }
    }

    public String readString(){
        String input = "";
        while(true)
        {
            try{
                TimeUnit.MILLISECONDS.sleep(500);
                Scanner scanner = new Scanner(System.in);
                input = scanner.nextLine();
                return input;
            } catch (Exception e){
                System.out.print("error / Invalid input, please enter another string: ");
                continue;
            }
        }
    }

    

    
}
