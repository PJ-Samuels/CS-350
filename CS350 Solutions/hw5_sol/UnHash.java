package hw5;

import java.security.*;

/***************************************************/
/* CS-350 Fall 2020 - Homework 5 - Code Solution   */
/* Author: Renato Mancuso (BU)                     */
/*                                                 */
/* Description: This class implements the logic of */
/*   a simple MD5 hash cracker. It takes in input  */
/*   an MD5 hash hexadecimal ASCII representation  */
/*   and uses a brute-force search to reverse the  */
/*   hash.                                         */
/*                                                 */
/***************************************************/

public class UnHash {

    public int unhash (String to_unhash) throws NoSuchAlgorithmException
    {
    	/* Construct a simple hasher class */
        Hash hasher = new Hash();
        
        /* Loop forever until a match is found */
        for(int cur = 0; ; ++cur) {
            String tmpHash = hasher.hash(cur);
            
            /* Does the current hash matches the target hash? */
            if(tmpHash.equals(to_unhash))
            	 /* Found it! Return right away. */
                return cur;
        }
    }

    /* Class test procedure. Take a MD5 hash via system paramters and reverse it. */
    public static void main(String[] args) throws NoSuchAlgorithmException 
    {
        String to_unhash = args[0];
        
        /* Construct dehasher */
        UnHash dehasher = new UnHash();
        
        /* Reverse hash and print result in one go */
        System.out.println(dehasher.unhash(to_unhash));
    }
}

/* END -- Q1BSR1QgUmVuYXRvIE1hbmN1c28= */
