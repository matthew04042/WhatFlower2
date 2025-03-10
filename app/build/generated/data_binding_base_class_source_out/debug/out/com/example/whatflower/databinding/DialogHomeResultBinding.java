// Generated by view binder compiler. Do not edit!
package com.example.whatflower.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.whatflower.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class DialogHomeResultBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final Button dialogButtonCancel;

  @NonNull
  public final Button dialogButtonOk;

  @NonNull
  public final ImageView dialogHomeImage;

  @NonNull
  public final TextView dialogTitle;

  private DialogHomeResultBinding(@NonNull LinearLayout rootView,
      @NonNull Button dialogButtonCancel, @NonNull Button dialogButtonOk,
      @NonNull ImageView dialogHomeImage, @NonNull TextView dialogTitle) {
    this.rootView = rootView;
    this.dialogButtonCancel = dialogButtonCancel;
    this.dialogButtonOk = dialogButtonOk;
    this.dialogHomeImage = dialogHomeImage;
    this.dialogTitle = dialogTitle;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static DialogHomeResultBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static DialogHomeResultBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.dialog_home_result, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static DialogHomeResultBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.dialog_button_cancel;
      Button dialogButtonCancel = ViewBindings.findChildViewById(rootView, id);
      if (dialogButtonCancel == null) {
        break missingId;
      }

      id = R.id.dialog_button_ok;
      Button dialogButtonOk = ViewBindings.findChildViewById(rootView, id);
      if (dialogButtonOk == null) {
        break missingId;
      }

      id = R.id.dialog_home_image;
      ImageView dialogHomeImage = ViewBindings.findChildViewById(rootView, id);
      if (dialogHomeImage == null) {
        break missingId;
      }

      id = R.id.dialog_title;
      TextView dialogTitle = ViewBindings.findChildViewById(rootView, id);
      if (dialogTitle == null) {
        break missingId;
      }

      return new DialogHomeResultBinding((LinearLayout) rootView, dialogButtonCancel,
          dialogButtonOk, dialogHomeImage, dialogTitle);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
