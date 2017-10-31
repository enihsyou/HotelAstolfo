package beans;

import com.google.gson.annotations.SerializedName;

public class GameEntry {
  @SerializedName("name")
  private String name;

  @SerializedName("final_price")
  private int price;

  @SerializedName("id")
  private int gameID;

  @SerializedName("header_image")
  private String headerImageURL;

  @SerializedName("large_capsule_image")
  private String largeCapsuleImageURL;
  @SerializedName("small_capsule_image")
  private String smallCapsuleImageURL;

  // @SerializedName("tags")
  // private List<String> tags;

  // public static final Type mapType = new TypeToken<Map<Integer, GameEntry>>() { }.getType(); // define generic type
}
