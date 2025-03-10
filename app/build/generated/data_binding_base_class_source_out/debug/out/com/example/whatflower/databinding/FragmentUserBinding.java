// Generated by view binder compiler. Do not edit!
package com.example.whatflower.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.whatflower.R;
import com.example.whatflower.ui.view.CharAvatarView;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentUserBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final CharAvatarView caUser;

  @NonNull
  public final RelativeLayout rlAddUser;

  @NonNull
  public final RelativeLayout rlLogout;

  @NonNull
  public final RelativeLayout rlUser;

  @NonNull
  public final TextView tvName;

  private FragmentUserBinding(@NonNull LinearLayout rootView, @NonNull CharAvatarView caUser,
      @NonNull RelativeLayout rlAddUser, @NonNull RelativeLayout rlLogout,
      @NonNull RelativeLayout rlUser, @NonNull TextView tvName) {
    this.rootView = rootView;
    this.caUser = caUser;
    this.rlAddUser = rlAddUser;
    this.rlLogout = rlLogout;
    this.rlUser = rlUser;
    this.tvName = tvName;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentUserBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentUserBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_user, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentUserBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.ca_user;
      CharAvatarView caUser = ViewBindings.findChildViewById(rootView, id);
      if (caUser == null) {
        break missingId;
      }

      id = R.id.rl_add_user;
      RelativeLayout rlAddUser = ViewBindings.findChildViewById(rootView, id);
      if (rlAddUser == null) {
        break missingId;
      }

      id = R.id.rl_logout;
      RelativeLayout rlLogout = ViewBindings.findChildViewById(rootView, id);
      if (rlLogout == null) {
        break missingId;
      }

      id = R.id.rl_user;
      RelativeLayout rlUser = ViewBindings.findChildViewById(rootView, id);
      if (rlUser == null) {
        break missingId;
      }

      id = R.id.tv_name;
      TextView tvName = ViewBindings.findChildViewById(rootView, id);
      if (tvName == null) {
        break missingId;
      }

      return new FragmentUserBinding((LinearLayout) rootView, caUser, rlAddUser, rlLogout, rlUser,
          tvName);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
