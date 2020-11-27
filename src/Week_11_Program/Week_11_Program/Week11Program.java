package Week_11_Program;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import javax.sound.sampled.SourceDataLine;

import sun.misc.Queue;

/**
 * Week 11/12 Program
 * 
 * @author vbarfield CS1131
 */
public class Week11Program {
    public static void main(String[] args) throws FileNotFoundException, QueueFullException,
     QueueEmptyException, IOException{
         if (args[0] == null) { throw new FileNotFoundException(); }
        
        File inputFile = new File(args[0]);
        FileReader inputFileReader = new FileReader(inputFile);

        char[] bufferSizeCharArray = new char[2];
        bufferSizeCharArray[0] = (char) inputFileReader.read();
        bufferSizeCharArray[1] = (char) inputFileReader.read();
        
        int bufferSize = Integer.parseInt(String.valueOf(bufferSizeCharArray));

        GenericQueue<Character> characterQueue = new GenericQueue<>(bufferSize);

        int character;
        while((character = inputFileReader.read()) != -1){
                characterQueue.enqueue((char) character);
                if (characterQueue.isFull()) { dumpQueue(characterQueue); }
        }
        dumpQueue(characterQueue);
    }

    private static void dumpQueue(QueueInterface queue) throws QueueEmptyException{
        StringBuilder outputString = new StringBuilder();
        while (!queue.isEmpty()){
            outputString.append(queue.dequeue());
        }
        System.out.print(String.valueOf(outputString) + "\n");
    }
}