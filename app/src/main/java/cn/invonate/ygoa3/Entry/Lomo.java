package cn.invonate.ygoa3.Entry;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liyangyang on 2018/3/13.
 */

public class Lomo {

    /**
     * total : 10
     * rows : [{"IS_ANONYMOUS":"0","LOMO_CONTENT":"MTI2NDU=","LOMO_ID":"d5092e4b-2655-11e8-9e21-1866daf6a144","LOMO_IMAGES":"","LOMO_VIDEO":"","PUBLISH_TIME":"2018-03-13 08:31:00.0","USER_CODE":"034488","USER_ID":"89A62CF1-C824-4AA0-AA6D-CA812EC35740","USER_NAME":"谢凌云","VALID_":"","images":[],"infoList":[],"info_num":"","stringImage":"","th_num":"","thumb_up":"0","user_photo":"personal/034488.jpg"},{"IS_ANONYMOUS":"0","LOMO_CONTENT":"5rKh5om+5Yiw6K+35YGH55Sz6K+35Y2V5Yqf6IO9","LOMO_ID":"18c14c4c-2371-11e8-9e21-1866daf6a144","LOMO_IMAGES":"","LOMO_VIDEO":"","PUBLISH_TIME":"2018-03-09 16:09:00.0","USER_CODE":"033148","USER_ID":"8a8a83e04f442a91014f4508a7c335e4","USER_NAME":"朱晓东","VALID_":"","images":[],"infoList":[],"info_num":"2","stringImage":"","th_num":"","thumb_up":"0","user_photo":"personal/033148.jpg"},{"IS_ANONYMOUS":"0","LOMO_CONTENT":"5rWL6K+V","LOMO_ID":"5bc24c8b-20d9-11e8-9e21-1866daf6a144","LOMO_IMAGES":"","LOMO_VIDEO":"lomo/034488/2018/03/06/6786be9b7fe74006944c2f21dfbb6ff1.mp4","PUBLISH_TIME":"2018-03-06 08:57:00.0","USER_CODE":"034488","USER_ID":"89A62CF1-C824-4AA0-AA6D-CA812EC35740","USER_NAME":"谢凌云","VALID_":"","images":[],"infoList":[],"info_num":"","stringImage":"","th_num":"","thumb_up":"0","user_photo":"personal/034488.jpg"},{"IS_ANONYMOUS":"0","LOMO_CONTENT":"","LOMO_ID":"0c6d5e17-20d9-11e8-9e21-1866daf6a144","LOMO_IMAGES":"","LOMO_VIDEO":"lomo/034488/2018/03/06/7f11ec2928f04401bfab5bed857e4265.mp4","PUBLISH_TIME":"2018-03-06 08:55:00.0","USER_CODE":"034488","USER_ID":"89A62CF1-C824-4AA0-AA6D-CA812EC35740","USER_NAME":"谢凌云","VALID_":"","images":[],"infoList":[],"info_num":"","stringImage":"","th_num":"","thumb_up":"0","user_photo":"personal/034488.jpg"},{"IS_ANONYMOUS":"0","LOMO_CONTENT":"5rWL6K+V","LOMO_ID":"de0b6a64-20d8-11e8-9e21-1866daf6a144","LOMO_IMAGES":"","LOMO_VIDEO":"lomo/034488/2018/03/06/c18919d4060e4c0f9fed8e25294a3df0.mp4","PUBLISH_TIME":"2018-03-06 08:54:00.0","USER_CODE":"034488","USER_ID":"89A62CF1-C824-4AA0-AA6D-CA812EC35740","USER_NAME":"谢凌云","VALID_":"","images":[],"infoList":[],"info_num":"","stringImage":"","th_num":"","thumb_up":"0","user_photo":"personal/034488.jpg"}]
     */

    private int total;
    private List<LomoBean> rows;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<LomoBean> getRows() {
        return rows;
    }

    public void setRows(List<LomoBean> rows) {
        this.rows = rows;
    }

    /**
     * 初始化图片
     */
    public void initImage() {
        for (LomoBean bean : rows) {
            ArrayList<String> list_imgs = new ArrayList<>();
            if ("".equals(bean.getLOMO_IMAGES())) {
                bean.setList_imgs(list_imgs);
                continue;
            }
            if (!bean.getLOMO_IMAGES().contains(",")) {
                list_imgs.add(bean.getLOMO_IMAGES());
            } else {
                String[] imgs = bean.getLOMO_IMAGES().split(",");
                for (int i = 0; i < imgs.length; i++) {
                    list_imgs.add(imgs[i]);
                }
            }
            bean.setList_imgs(list_imgs);
        }
    }

    public static class LomoBean implements Serializable{
        /**
         * IS_ANONYMOUS : 0
         * LOMO_CONTENT : MTI2NDU=
         * LOMO_ID : d5092e4b-2655-11e8-9e21-1866daf6a144
         * LOMO_IMAGES :
         * LOMO_VIDEO :
         * PUBLISH_TIME : 2018-03-13 08:31:00.0
         * USER_CODE : 034488
         * USER_ID : 89A62CF1-C824-4AA0-AA6D-CA812EC35740
         * USER_NAME : 谢凌云
         * VALID_ :
         * images : []
         * infoList : []
         * info_num :
         * stringImage :
         * th_num :
         * thumb_up : 0
         * user_photo : personal/034488.jpg
         */

        private int IS_ANONYMOUS;
        private String LOMO_CONTENT;
        private String LOMO_ID;
        private String LOMO_IMAGES;
        private String LOMO_VIDEO;
        private String PUBLISH_TIME;
        private String USER_CODE;
        private String USER_ID;
        private String USER_NAME;
        private String VALID_;
        private int info_num;
        private String stringImage;
        private int th_num;
        private String thumb_up;
        private String user_photo;
        private List<String> images;
        private List<String> infoList;
        private ArrayList<String> list_imgs;

        public int getIS_ANONYMOUS() {
            return IS_ANONYMOUS;
        }

        public void setIS_ANONYMOUS(int IS_ANONYMOUS) {
            this.IS_ANONYMOUS = IS_ANONYMOUS;
        }

        public String getLOMO_CONTENT() {
            return LOMO_CONTENT;
        }

        public void setLOMO_CONTENT(String LOMO_CONTENT) {
            this.LOMO_CONTENT = LOMO_CONTENT;
        }

        public String getLOMO_ID() {
            return LOMO_ID;
        }

        public void setLOMO_ID(String LOMO_ID) {
            this.LOMO_ID = LOMO_ID;
        }

        public String getLOMO_IMAGES() {
            return LOMO_IMAGES;
        }

        public void setLOMO_IMAGES(String LOMO_IMAGES) {
            this.LOMO_IMAGES = LOMO_IMAGES;
        }

        public String getLOMO_VIDEO() {
            return LOMO_VIDEO;
        }

        public void setLOMO_VIDEO(String LOMO_VIDEO) {
            this.LOMO_VIDEO = LOMO_VIDEO;
        }

        public String getPUBLISH_TIME() {
            return PUBLISH_TIME;
        }

        public void setPUBLISH_TIME(String PUBLISH_TIME) {
            this.PUBLISH_TIME = PUBLISH_TIME;
        }

        public String getUSER_CODE() {
            return USER_CODE;
        }

        public void setUSER_CODE(String USER_CODE) {
            this.USER_CODE = USER_CODE;
        }

        public String getUSER_ID() {
            return USER_ID;
        }

        public void setUSER_ID(String USER_ID) {
            this.USER_ID = USER_ID;
        }

        public String getUSER_NAME() {
            return USER_NAME;
        }

        public void setUSER_NAME(String USER_NAME) {
            this.USER_NAME = USER_NAME;
        }

        public String getVALID_() {
            return VALID_;
        }

        public void setVALID_(String VALID_) {
            this.VALID_ = VALID_;
        }

        public int getInfo_num() {
            return info_num;
        }

        public void setInfo_num(int info_num) {
            this.info_num = info_num;
        }

        public String getStringImage() {
            return stringImage;
        }

        public void setStringImage(String stringImage) {
            this.stringImage = stringImage;
        }

        public int getTh_num() {
            return th_num;
        }

        public void setTh_num(int th_num) {
            this.th_num = th_num;
        }

        public String getThumb_up() {
            return thumb_up;
        }

        public void setThumb_up(String thumb_up) {
            this.thumb_up = thumb_up;
        }

        public String getUser_photo() {
            return user_photo;
        }

        public void setUser_photo(String user_photo) {
            this.user_photo = user_photo;
        }

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }

        public List<String> getInfoList() {
            return infoList;
        }

        public void setInfoList(List<String> infoList) {
            this.infoList = infoList;
        }

        public ArrayList<String> getList_imgs() {
            return list_imgs;
        }

        public void setList_imgs(ArrayList<String> list_imgs) {
            this.list_imgs = list_imgs;
        }

    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
