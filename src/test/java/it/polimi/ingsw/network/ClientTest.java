package it.polimi.ingsw.network;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class ClientTest {

    @Test
    public void getSymbolsTest(){
        String random = Client.getRandomSymbol();
        for (int i = 0; i < random.length()-1; i ++)
            assertTrue(random.charAt(i) != random.charAt(i+1));
    }

}
