package nz.co.tmsandbox.webinteractivities;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

public class RandomDataGenerator implements IWebActions {
    /**
     * Enum which is used to generate and store random value types
     */
    public enum valueType {
        RANDOMSTRING, RANDOMNUMBER
    }

     private final char[] letters = "adefaeo!$%&gate ghilmnoprst ".toCharArray();

    private final char[] numbers = "1234567890".toCharArray();
    private final ThreadLocal<Map<String, String>> storeValues = ThreadLocal.withInitial(LinkedHashMap::new);


    /**
     * Generates random value for a string or number with variable length and stores in a variable
     *
     * @param genValueFor                     is an enum of valueType
     * @param length                          specify the length of random string or number
     * @param storeGeneratedValueInIdentifier pass the variable in which value should be stored, can be used to retrieve later in the scenario if needed
     * @return random generated string or number value
     */
    public String generate(valueType genValueFor, int length, String storeGeneratedValueInIdentifier) {
        switch (genValueFor) {
            case RANDOMSTRING:
                storeValues.get().put(storeGeneratedValueInIdentifier, getRandomString(length));
                break;
            case RANDOMNUMBER:
                storeValues.get().put(storeGeneratedValueInIdentifier, getRandomNumber(length));
                break;
            default:
                debugMessageLogger.logInformation("Please enter valid type to RandamDataGenerator");
        }
        return storeValues.get().get(storeGeneratedValueInIdentifier);
    }

    private String getRandomString(int length) {
        return getRandom(length, letters);
    }

    private String getRandomNumber(int length) {
        return getRandom(length, numbers);
    }

    private String getRandom(int length, char[] chars) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        return sb.toString();
    }

}
