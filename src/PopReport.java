import java.io.*; // all of java io package imported
import java.util.ArrayList; // util package's ArrayList class imported
import java.util.Scanner; // util package's Scanner class imported

/**
 *  Author: Salman
 *
 *  This program reads data from a .csv file, processes it and outputs to a .txt file
 */
public class PopReport {

    private static PrintWriter popReport; // Variable to contain PrintWriter named popReport created

    /**
     Main handles the driver code for the program. Inputs are taken,
     processed and outputted in this method.
     */
    public static void main(String[] args) throws IOException {

        File file = new File("CountryData.csv"); // csv file called
        Scanner countryData = new Scanner(file); // file passed to Scanner called countryData

        //first two lines of the file (Headers) skipped
        countryData.nextLine();
        countryData.nextLine();

        ArrayList<String> rawLines = new ArrayList<>(); // ArrayList called rawLines created

        //lines read from file and stored in rawLines ArrayList
        while (countryData.hasNext())
            rawLines.add(countryData.nextLine());

        countryData.close(); // file countryData closed

        //Arrays declared to hold formatted info read from CountryData file in a parallel manner
        ArrayList<String> code = new ArrayList<>();
        ArrayList<String> name = new ArrayList<>();
        ArrayList<Integer> population = new ArrayList<>();
        ArrayList<Double> lifeExpectancy = new ArrayList<>();
        ArrayList<String> continent = new ArrayList<>();
        ArrayList<String> region = new ArrayList<>();
        ArrayList<Integer> landArea = new ArrayList<>();

        //lines read previously are split, filtered and stored in parallel arrays
        for (int i = 0; i < rawLines.size(); i++) {
            String[] tokens = cleanUpRecord(rawLines.get(i));
            if (!(Integer.parseInt(tokens[2]) == 0) && !(Double.parseDouble(tokens[3]) == 0) &&
                    !((tokens[4]).equals("Antarctica"))) {
                code.add(tokens[0]);
                name.add(tokens[1]);
                population.add(Integer.parseInt(tokens[2]));
                lifeExpectancy.add(Double.parseDouble(tokens[3]));
                continent.add(tokens[4]);
                region.add(tokens[5]);
                landArea.add(Integer.parseInt(tokens[6]));
            }
        }

        popReport = new PrintWriter("PopReport.txt"); //file name passed to PrintWriter to output to


        //outputs are made through PrintWriter

        popReport.println("WORLD POPULATION REPORT\n\n");

        doTable1(continent, code, name, region, lifeExpectancy);
        doTable2(continent ,population, landArea, name);
        doTable3(continent, lifeExpectancy);

        popReport.println("\n\n<<<< END OF REPORT >>>>");

        popReport.close(); //PrintWriter is closed

        System.out.println("File has been written.");

    }

    /**
     * This method extrats the useful parts from every line read
     * @param line receives a single line read from countryData.csv every time
     * @return an array containing cleand up information from the line received
     */
    public static String[] cleanUpRecord (String line){

        StringBuilder str = new StringBuilder(line); //received line string has been passed to String Builder str


        //unwanted parts are deleted from the received str

        for (int i = 0; i < str.length(); i++){
            if (str.charAt(i) == '\'')
                str.deleteCharAt(i);
        }

        for (int i = 0; i < str.length(); i++){
            if (str.charAt(i) == '(') {
                str.delete(0, (i + 1));
                break;
            }
        }

        for (int i = (str.length() - 1); i >= 0; i--){
            if (str.charAt(i) == ')') {
                str.delete(i, (line.length()));
                break;
            }
        }

        String[] tokens = (str.toString()).split(","); //str is split at every ',' (comma)


        //names and regions with more than 16 characters are truncated

        if (tokens[1].length() > 16)
            tokens[1] = tokens[1].substring(0,16);

        if (tokens[5].length() > 16)
            tokens[5] = tokens[5].substring(0,16);

        return tokens;

    }

    /**
     * Writes the first table (Life Expectancy in Africa by Country) to the .txt file
     * @param continent receives a parallel array containing countries' continents
     * @param code receives a parallel array containing countries' codes
     * @param name receives a parallel array containing countries' names
     * @param region receives a parallel array containing countries' regions
     * @param lifeExpectancy receives a parallel array containing countries' people's life expectancy
     * @throws IOException as PrintWriter is being used in this method
     */
    public static void doTable1 (ArrayList<String> continent, ArrayList<String> code, ArrayList<String> name,
                                 ArrayList<String> region, ArrayList<Double> lifeExpectancy)  throws IOException {

        popReport.println("TABLE 1: Life Expectancy in Africa by Country\n\n" +
                "Code Name             Region           LifeExp\n" +
                "---- ---------------- ---------------- -------");
        for (int i = 0; i < continent.size(); i++) {
            if ((continent.get(i)).equals("Africa"))
                popReport.printf("%-5s%-17s%-17s %.1f\n", code.get(i), name.get(i), region.get(i),
                        lifeExpectancy.get(i));
        }
    }

    /**
     * Calculates some statistics and writes the second table (Europe Population & Land Area) to the .txt file
     * @param continent receives a parallel array containing countries' continents
     * @param population receives a parallel array containing countries' populations
     * @param landArea receives a parallel array containing countries' land areas
     * @param name receives a parallel array containing countries' names
     * @throws IOException as PrintWriter is being used in this method
     */
    public static void doTable2 (ArrayList<String> continent, ArrayList<Integer> population,
                                 ArrayList<Integer> landArea, ArrayList<String> name) throws IOException {

        popReport.println("\n\nTABLE 2: Europe Population & Land Area (37 countries)\n\nPopulation:");
        int maxVal = 0; //declared to store maximum value
        int maxIndex = 0; //declared to store index of maximum value
        int minVal = Integer.MAX_VALUE; //declared to store minimum value
        int minIndex = 0; //declared to store index of minimum value
        int tot = 0; //declared to hold running total
        int countEurope = 0; //declared to count no. of countries in Europe

        //calculates minimum and maximum population values along with total population and total no. of countries
        for (int i = 0; i < continent.size(); i++){
            if ((continent.get(i)).equals("Europe")){
                if ((population.get(i)) > maxVal) {
                    maxVal = population.get(i);
                    maxIndex = i;
                }
                if ((population.get(i)) < minVal){
                    minVal = population.get(i);
                    minIndex = i;
                }
                tot += population.get(i);
                countEurope++;
            }
        }

        //writes above calculated information to file
        popReport.printf("\t%-9s%,15d  %s\n" +
                        "\t%-9s%,15d  %s\n" +
                        "\t%-9s%,15.0f\n" +
                        "\t%-9s%,15d\n", "Largest:", maxVal, name.get(maxIndex), "Smallest:", minVal,
                name.get(minIndex), "Mean:", ((double)tot/countEurope), "Total:", tot);


        popReport.println("Land Area (in sq km):");

        //reassigns values to previously declared variables to perform calculations for land areas
        maxVal = 0;
        maxIndex = 0;
        minVal = Integer.MAX_VALUE;
        minIndex = 0;
        tot = 0;

        //calculates minimum and maximum land area values along with total population and total no. of countries
        for (int i = 0; i < continent.size(); i++){
            if ((continent.get(i)).equals("Europe")){
                if ((landArea.get(i)) > maxVal) {
                    maxVal = landArea.get(i);
                    maxIndex = i;
                }
                if ((landArea.get(i)) < minVal){
                    minVal = landArea.get(i);
                    minIndex = i;
                }
                tot += landArea.get(i);
            }
        }

        //writes above calculated information to file
        popReport.printf("\t%-9s%,15d  %s\n" +
                        "\t%-9s%,15d  %s\n" +
                        "\t%-9s%,15.0f\n" +
                        "\t%-9s%,15d\n", "Largest:", maxVal, name.get(maxIndex), "Smallest:", minVal,
                name.get(minIndex), "Mean:", ((double)tot/countEurope), "Total:", tot);

    }

    /**
     * Calculates some statistics and writes the third table (Life Expectancy by Continent) to the .txt file
     * @param continent receives a parallel array containing countries' continents
     * @param lifeExpectancy receives a parallel array containing countries' people's life expectancy
     * @throws IOException as PrintWriter is being used in this method
     */
    public static void doTable3 (ArrayList<String> continent, ArrayList<Double> lifeExpectancy) throws IOException {

        popReport.println("\n\nTABLE 3: Life Expectancy by Continent\n");

        //declares a string with continent names
        String[] contName = {"Africa", "Asia", "Europe", "North America", "Oceania", "South America"};

        double[] tempMin = new double[contName.length]; //to hold minimum life expectancy for every continent
        double[] tempMax = new double[contName.length]; //to hold maximum life expectancy for every continent
        double[] totalLE = new double[contName.length]; //to hold total of life expectancies for every continent
        int[] nCountries = new int[contName.length]; //to hold no. of countries for each continent
        double[] avgLE = new double[contName.length]; //to hold average life expectancy for each continent

        //gives initial values to above declared arrays' elements
        for (int i = 0; i < contName.length; i++){
            tempMin[i] = lifeExpectancy.get(0);
            tempMax[i] = lifeExpectancy.get(0);
            totalLE[i] = 0.0;
            nCountries[i] = 0;
        }

        //calculates maximum and minimum life expectancies along with no. of countries for every continent
        for (int i = 0; i < continent.size(); i++){
            if ((continent.get(i)).equals("Africa")){
                totalLE[0] += lifeExpectancy.get(i);
                nCountries[0]++;
                if (lifeExpectancy.get(i) < tempMin[0]) {
                    tempMin[0] = lifeExpectancy.get(i);
                } else if (lifeExpectancy.get(i) > tempMax[0]){
                    tempMax[0] = lifeExpectancy.get(i);
                }
            } else if ((continent.get(i)).equals("Asia")){
                totalLE[1] += lifeExpectancy.get(i);
                nCountries[1]++;
                if (lifeExpectancy.get(i) < tempMin[1]) {
                    tempMin[1] = lifeExpectancy.get(i);
                } else if (lifeExpectancy.get(i) > tempMax[1]) {
                    tempMax[1] = lifeExpectancy.get(i);
                }
            } else if ((continent.get(i)).equals("Europe")){
                totalLE[2] += lifeExpectancy.get(i);
                nCountries[2]++;
                if (lifeExpectancy.get(i) < tempMin[2]) {
                    tempMin[2] = lifeExpectancy.get(i);
                } else if (lifeExpectancy.get(i) > tempMax[2]){
                    tempMax[2] = lifeExpectancy.get(i);
                }
            } else if ((continent.get(i)).equals("North America")){
                totalLE[3] += lifeExpectancy.get(i);
                nCountries[3]++;
                if (lifeExpectancy.get(i) < tempMin[3]) {
                    tempMin[3] = lifeExpectancy.get(i);
                } else if (lifeExpectancy.get(i) > tempMax[3]){
                    tempMax[3] = lifeExpectancy.get(i);
                }
            } else if ((continent.get(i)).equals("Oceania")){
                totalLE[4] += lifeExpectancy.get(i);
                nCountries[4]++;
                if (lifeExpectancy.get(i) < tempMin[4]) {
                    tempMin[4] = lifeExpectancy.get(i);
                } else if (lifeExpectancy.get(i) > tempMax[4]){
                    tempMax[4] = lifeExpectancy.get(i);
                }
            } else if ((continent.get(i)).equals("South America")){
                totalLE[5] += lifeExpectancy.get(i);
                nCountries[5]++;
                if (lifeExpectancy.get(i) < tempMin[5]) {
                    tempMin[5] = lifeExpectancy.get(i);
                } else if (lifeExpectancy.get(i) > tempMax[5]){
                    tempMax[5] = lifeExpectancy.get(i);
                }
            }

        }

        //calculates average life expectancy for each continent
        for (int i = 0; i < contName.length; i++){
            avgLE[i] = totalLE[i]/ (double) nCountries[i];
        }

        popReport.println("\tContinent     Highest Lowest Average\n" +
                "\t------------- ------- ------ -------");

        //writes above calculations through PrintWriter
        for (int i = 0; i < contName.length; i++){
            popReport.printf("\t%-14s %-7.1f %-6.1f %.1f\n", contName[i], tempMax[i], tempMin[i], avgLE[i]);
        }

    }

}
