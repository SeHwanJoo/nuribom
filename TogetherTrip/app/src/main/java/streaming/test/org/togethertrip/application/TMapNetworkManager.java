package streaming.test.org.togethertrip.application;

/**
 * Created by taehyung on 2017-10-22.
 */

public class TMapNetworkManager {
    private static TMapNetworkManager instance;
    public static TMapNetworkManager getInstance(){
        if(instance == null){
            instance = new TMapNetworkManager();
        }
        return instance;
    }
}
