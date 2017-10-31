package beans;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SteamList {
  @SerializedName("large_capsules")
  private List<GameEntry> largeCapsules;
  @SerializedName("featured_win")
  private List<GameEntry> featuredWin;
  @SerializedName("featured_mac")
  private List<GameEntry> featuredMac;
  @SerializedName("featured_linux")
  private List<GameEntry> featuredLinux;
}
