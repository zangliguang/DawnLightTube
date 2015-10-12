/*
 * Copyright (C) 2014 Pedro Vicente Gómez Sánchez.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package zang.liguang.tube.fragment;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import zang.liguang.tube.R;
import zang.liguang.tube.utils.Options;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Picasso;

/**
 * Fragment implementation created to show a poster inside an ImageView widget.
 *
 * @author Pedro Vicente Gómez Sánchez.
 */
@EFragment(R.layout.fragment_movie_poster)
public class MoviePosterFragment extends Fragment {

	@ViewById(R.id.iv_thumbnail)
	ImageView thumbnailImageView;

	private String videoPosterThumbnail;
	private String posterTitle;

  /**
   * Override method used to initialize the fragment.
   */
//  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
//      Bundle savedInstanceState) {
//    Picasso.with(getActivity())
//        .load(videoPosterThumbnail)
//        .placeholder(R.drawable.xmen_placeholder)
//        .into(thumbnailImageView);
//    return view;
//  }

  /**
   * Show the poster image in the thumbnailImageView widget.
   */
  public void setPoster(String videoPosterThumbnail) {
    this.videoPosterThumbnail = videoPosterThumbnail;
    ImageLoader.getInstance().displayImage(videoPosterThumbnail, thumbnailImageView, Options.getListOptions());
  }

  /**
   * Store the poster title to show it when the thumbanil view is clicked.
   */
  public void setPosterTitle(String posterTitle) {
    this.posterTitle = posterTitle;
  }

  /**
   * Method triggered when the iv_thumbnail widget is clicked. This method shows a toast with the
   * poster information.
   */
//  @OnClick(R.id.iv_thumbnail) void onThubmnailClicked() {
//    Toast.makeText(getActivity(), posterTitle, Toast.LENGTH_SHORT).show();
//  }
}