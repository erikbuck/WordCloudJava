import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

public class WordCloudChallenge {

    public static InputStream getTerminalInputStream() {
        System.out.println("Enter a list of words. Terminate the list of words\n" +
                "with Control-z on Windows or Control-d on Linux/Unix/Mac:");
        return System.in;
    }

    
    public static InputStream getFileInputStream() throws FileNotFoundException {
        return new FileInputStream("in.txt");
    }

    public static InputStream getStringInputStream() {
        String wordsString = "From the moment that the French defenses at Sedan and on the Meuse were broken at the end of the second week of May, only a rapid retreat to Amiens and the south could have saved the British and French Armies who had entered Belgium at the appeal of the Belgian King; but this strategic fact was not immediately realized. The French High Command hoped they would be able to close the gap, and the Armies of the north were under their orders. Moreover, a retirement of this kind would have involved almost certainly the destruction of the fine Belgian Army of over 20 divisions and the abandonment of the whole of Belgium. Therefore, when the force and scope of the German penetration were realized and when a new French Generalissimo, General Weygand, assumed command in place of General Gamelin, an effort was made by the French and British Armies in Belgium to keep on holding the right hand of the Belgians and to give their own right hand to a newly created French Army which was to have advanced across the Somme in great strength to grasp it."+
        "However, the German eruption swept like a sharp scythe around the right and rear of the Armies of the north. Eight or nine armored divisions, each of about four hundred armored vehicles of different kinds, but carefully assorted to be complementary and divisible into small self-contained units, cut off all communications between us and the main French Armies. It severed our own communications for food and ammunition, which ran first to Amiens and afterwards through Abbeville, and it shore its way up the coast to Boulogne and Calais, and almost to Dunkirk. Behind this armored and mechanized onslaught came a number of German divisions in lorries, and behind them again there plodded comparatively slowly the dull brute mass of the ordinary German Army and German people, always so ready to be led to the trampling down in other lands of liberties and comforts which they have never known in their own."+
        "I have said this armored scythe-stroke almost reached Dunkirk-almost but not quite. Boulogne and Calais were the scenes of desperate fighting. The Guards defended Boulogne for a while and were then withdrawn by orders from this country. The Rifle Brigade, the 60th Rifles, and the Queen Victoria’s Rifles, with a battalion of British tanks and 1,000 Frenchmen, in all about four thousand strong, defended Calais to the last. The British Brigadier was given an hour to surrender. He spurned the offer, and four days of intense street fighting passed before silence reigned over Calais, which marked the end of a memorable resistance. Only 30 unwounded survivors were brought off by the Navy, and we do not know the fate of their comrades. Their sacrifice, however, was not in vain. At least two armored divisions, which otherwise would have been turned against the British Expeditionary Force, had to be sent to overcome them. They have added another page to the glories of the light divisions, and the time gained enabled the Graveline water lines to be flooded and to be held by the French troops.";
        return new ByteArrayInputStream(wordsString.getBytes());
    }

    public static void main(String[] args) throws Exception {
        WordCounter wordCounter = new WordCounter();

        InputStream wordsStream = getFileInputStream();
        String filterString = "I of and the to a an not be in by but or as at if it is " +
                "be on was will with for do no it that";
        InputStream filterStream = new ByteArrayInputStream(filterString.getBytes());

        wordCounter.countWordsFromInputStream(wordsStream);
        wordCounter.filterWordsFromInputStream(filterStream);
        List<HashMap.Entry<String, Integer>> topWords = wordCounter.getTopWords(42);

        WordCloudMaker wcm = new WordCloudMaker();

        for(HashMap.Entry<String, Integer> entry : topWords) {
            for(int i = 0; i < entry.getValue(); i++) {
                wcm.addWord(entry.getKey());
            }
        }
        wcm.generateWordCloud(Paths.get("cloud.png"));
    }
}
