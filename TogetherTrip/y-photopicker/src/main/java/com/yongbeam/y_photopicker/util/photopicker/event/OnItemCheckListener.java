package com.yongbeam.y_photopicker.util.photopicker.event;


import com.yongbeam.y_photopicker.util.photopicker.entity.Photo;

public interface OnItemCheckListener {

  /***
   *
   * @param position Select Image postion
   * @param path     Select image path
   *@param isCheck   Image status
   * @param selectedItemCount  Select image count
   * @return enable check
   */
  boolean OnItemCheck(int position, Photo path, boolean isCheck, int selectedItemCount);

}
