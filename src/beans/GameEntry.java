package beans;

import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class GameEntry {
  @SerializedName("name")
  private String name;

  @SerializedName("header")
  private String headerImageURL;

  @SerializedName("main_capsule")
  private String capsuleImageURL;

  @SerializedName("tags")
  private List<String> tags;

  public static final Type mapType = new TypeToken<Map<Integer, GameEntry>>() { }.getType(); // define generic type
}
