package streaming.test.org.togethertrip.datas.tmapapi;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Created by user1 on 2017-10-23.
 */

public class GeometryDeserializer implements JsonDeserializer<TmapGeometry> {
    @Override
    public TmapGeometry deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = element.getAsJsonObject();
        TmapGeometry geometry = new TmapGeometry();
        geometry.type = obj.get("type").getAsString();
        JsonArray array = obj.get("coordinates").getAsJsonArray();
        if(geometry.type.equals("LineString")){
            geometry.coordinates = new double[array.size() * 2];
            for(int i=0; i<array.size(); i++){
                JsonArray sub = array.get(i).getAsJsonArray();
                geometry.coordinates[i*2] = sub.get(0).getAsDouble();
                geometry.coordinates[i*2+1] = sub.get(1).getAsDouble();
            }
        }else if(geometry.type.equals("Point")){
            geometry.coordinates = new double[array.size()];
            for(int i =0;i<array.size();i++){
                geometry.coordinates[i] = array.get(i).getAsDouble();
            }
        }

        return geometry;
    }
}
