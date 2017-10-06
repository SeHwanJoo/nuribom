package com.yongbeam.y_photopicker.util.photopicker.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;

import com.yongbeam.y_photopicker.util.photopicker.PhotoPickerActivity;

public class YPhotoPickerIntent extends Intent implements Parcelable {

  private YPhotoPickerIntent() {
  }

  private YPhotoPickerIntent(Intent o) {
    super(o);
  }

  private YPhotoPickerIntent(String action) {
    super(action);
  }

  private YPhotoPickerIntent(String action, Uri uri) {
    super(action, uri);
  }

  private YPhotoPickerIntent(Context packageContext, Class<?> cls) {
    super(packageContext, cls);
  }

  public YPhotoPickerIntent(Context packageContext) {
    super(packageContext, PhotoPickerActivity.class);
  }

  public void setMaxSelectCount(int photoCount) {
    this.putExtra(PhotoPickerActivity.EXTRA_MAX_COUNT, photoCount);
  }

  public void setShowCamera(boolean showCamera) {
    this.putExtra(PhotoPickerActivity.EXTRA_SHOW_CAMERA, showCamera);
  }

  public void setShowGif(boolean showGif) {
    this.putExtra(PhotoPickerActivity.EXTRA_SHOW_GIF, showGif);
  }

  public void setMaxGrideItemCount(int grideCount){
    this.putExtra(PhotoPickerActivity.EXTRA_MAX_GRIDE_ITEM_COUNT, grideCount);
  }

  public void setSelectCheckBox(boolean isCheckbox){
    this.putExtra(PhotoPickerActivity.EXTRA_CHECK_BOX_ONLY, isCheckbox);
  }

}
