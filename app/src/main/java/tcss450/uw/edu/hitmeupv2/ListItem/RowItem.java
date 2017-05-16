package tcss450.uw.edu.hitmeupv2.ListItem;

/**
 * Created by Shema Rezanejad on 5/9/2017.
 *
 * Custom Item in a row. Creates an item with an image,
 * title and subtitle.
 */

public class RowItem {
    /**
     * Profile pic id.
     */
    private int mImgId;
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
    public RowItem(int imgId, String title, String subItem) {
        this.mImgId = imgId;
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
    public int getmImgId() {
        return mImgId;
    }

    /**
     * Setter.
     * @param mImgId set img id
     */
    public void setmImgId(int mImgId) {
        this.mImgId = mImgId;
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
    public int getImgId() {
        return mImgId;


    }
    @Override
    public String toString() {
        return mTitle + "\n" + mSubItem;
    }
}
