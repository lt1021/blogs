package com.blog.microsoul.weapi.poi;

import org.apache.poi.ss.usermodel.ClientAnchor;

/**
 * @author xtiger (xtiger@microsoul.com) 2015-04-22 19:37
 */
public interface POIHelper {

    void createPicture(ClientAnchor anchor, int width, int height, byte[] pictureData, int insidePictureCount);

    void createPicture(int col, int row, int px1, int py1, int width, int height, byte[] pictureData, int insidePictureCount);

    void copyCell(int rowStart, int colStart, int colEnd, int dataRows, int offset, int count);

}
