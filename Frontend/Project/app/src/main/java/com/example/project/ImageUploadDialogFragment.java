package com.example.project;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.project.R;

import java.io.IOException;

public class ImageUploadDialogFragment extends DialogFragment {

    private ImageView imageView;
    private Button selectImageButton;
    private Button uploadImageButton;

    private Bitmap bitmap;
    private int userId;

    private int teamId;

    private boolean isUser;

    private ImageUploadListener uploadListener;

    private static final int PICK_IMAGE_REQUEST = 1;

    private ActivityResultLauncher<String> mGetContent;

    public ImageUploadDialogFragment(boolean isUser, int teamId) {
        this.isUser = isUser;
        this.teamId = teamId;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);


        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_image_upload_dialog, null);
        dialog.setContentView(view);


        if (dialog.getWindow() != null) {
            WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setAttributes(params);
        }

        return dialog;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        if (result != null) {
                            try {
                                bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), result);
                                Bitmap circularBitmap = ImageHelper.getCircularBitmap(bitmap);
                                imageView.setImageBitmap(circularBitmap);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_upload_dialog, container, false);



        imageView = view.findViewById(R.id.imageView);
        selectImageButton = view.findViewById(R.id.selectImageButton);
        uploadImageButton = view.findViewById(R.id.uploadImageButton);

        selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        uploadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userId = Integer.parseInt(SharedPrefsUtil.getUserId(getContext()));
                ImageUploader uploadManager = new ImageUploader();

                if(isUser == true) {
                    uploadManager.uploadUserImage(getContext(), userId, bitmap);
                }
                else{
                    uploadManager.uploadTeamImage(getContext(), teamId, bitmap);
                }

                dismiss();

                if(getActivity() != null && getActivity() instanceof RegistrationActivity) {
                    ((RegistrationActivity) getActivity()).onImageUploadDismissed();
                }
            }
        });

        return view;
    }

    private void openFileChooser() {
        mGetContent.launch("image/*");
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if (uploadListener != null) {
            uploadListener.onImageUploadDismissed();
        }
    }


    public void setImageUploadListener(ImageUploadListener listener) {
        this.uploadListener = listener;
    }

    public interface ImageUploadListener {
        void onImageUploadDismissed();
    }
}