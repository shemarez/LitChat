package tcss450.uw.edu.hitmeupv2.ListItem;

/**
 * Created by Shema Rezanejad on 5/9/2017.
 *
 * Custom Item in a row. Creates an item with an image,
 * title and subtitle.
 */

public class RowItem {
    /**
     * Profile pic path
     */
    private String mImgPath;
    /**
     * Name of friend.
     */
    private String mTitle;
    /**
     * SubItem of every Row.
     */
    private String mSubItem;

    /**
     * RowItem Overloaded Constructer.
     * @param imgId profile picture id.
     * @param title friend name
     * @param subItem subItem of every item
     */
    public RowItem(String imgId, String title, String subItem) {
        this.mImgPath = imgId;
        this.mTitle = title;
        this.mSubItem = subItem;
    }

    /**
     * Constructor if there is no img.
     * @param title friend name
     * @param subItem subItem of every it
     */
    public RowItem(String title, String subItem) {
        this.mTitle = title;
        this.mSubItem = subItem;
    }

    /**
     * Getter.
     * @return imgid
     */
    public String getmImgPath() {
        return mImgPath;
    }

    /**
     * Setter.
     * @param mImgPath set img id
     */
    public void setmImgPath(String mImgPath) {
        this.mImgPath = mImgPath;
    }

    /**
     * Getter.
     * @return title
     */
    public String getmTitle() {
        return mTitle;
    }

    /**
     * Setter
     * @param mTitle set title
     */
    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    /**
     * Getter.
     * @return subitem
     */
    public String getmSubItem() {
        return mSubItem;
    }

    /**
     * Setter.
     * @param mSubItem set subitem
     */
    public void setmSubItem(String mSubItem) {
        this.mSubItem = mSubItem;
    }

    /**
     * Getter.
     * @return img id
     */
    public String getImgPath() {
        return mImgPath;


    }
    @Override
    public String toString() {
        return mTitle + "\n" + mSubItem;
    }
}
