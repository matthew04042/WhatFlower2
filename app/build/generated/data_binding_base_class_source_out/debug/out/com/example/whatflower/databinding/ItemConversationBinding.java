// Generated by view binder compiler. Do not edit!
package com.example.whatflower.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public final class ItemConversationBinding implements ViewBinding {
  @NonNull
  private final RelativeLayout rootView;

  @NonNull
  public final CharAvatarView caConverAvatar;

  @NonNull
  public final TextView textViewLastMessage;

  @NonNull
  public final TextView textViewUser;

  @NonNull
  public final TextView tvLastTime;

  private ItemConversationBinding(@NonNull RelativeLayout rootView,
      @NonNull CharAvatarView caConverAvatar, @NonNull TextView textViewLastMessage,
      @NonNull TextView textViewUser, @NonNull TextView tvLastTime) {
    this.rootView = rootView;
    this.caConverAvatar = caConverAvatar;
    this.textViewLastMessage = textViewLastMessage;
    this.textViewUser = textViewUser;
    this.tvLastTime = tvLastTime;
  }

  @Override
  @NonNull
  public RelativeLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ItemConversationBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ItemConversationBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.item_conversation, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ItemConversationBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.ca_conver_avatar;
      CharAvatarView caConverAvatar = ViewBindings.findChildViewById(rootView, id);
      if (caConverAvatar == null) {
        break missingId;
      }

      id = R.id.textViewLastMessage;
      TextView textViewLastMessage = ViewBindings.findChildViewById(rootView, id);
      if (textViewLastMessage == null) {
        break missingId;
      }

      id = R.id.textViewUser;
      TextView textViewUser = ViewBindings.findChildViewById(rootView, id);
      if (textViewUser == null) {
        break missingId;
      }

      id = R.id.tv_last_time;
      TextView tvLastTime = ViewBindings.findChildViewById(rootView, id);
      if (tvLastTime == null) {
        break missingId;
      }

      return new ItemConversationBinding((RelativeLayout) rootView, caConverAvatar,
          textViewLastMessage, textViewUser, tvLastTime);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
