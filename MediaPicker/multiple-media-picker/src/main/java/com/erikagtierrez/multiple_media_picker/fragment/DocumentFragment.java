package com.erikagtierrez.multiple_media_picker.fragment;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.erikagtierrez.multiple_media_picker.R;
import com.erikagtierrez.multiple_media_picker.adapter.BucketsAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DocumentFragment extends Fragment {

    private static RecyclerView recyclerView;
    private BucketsAdapter mAdapter;
    private List<String> documentNames = new ArrayList<>();
    private List<String> documentPathList =new ArrayList<>();
    public static List<String> documentList = new ArrayList<>();
    public static List<Boolean> selected=new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Bucket names reloaded
        documentPathList.clear();
        documentList.clear();
        documentNames.clear();
        getDocumentBuckets();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_grid, container, false);
        recyclerView = v.findViewById(R.id.recycler_view);
        populateRecyclerView();
        return v;
    }

    private void populateRecyclerView() {
        mAdapter = new BucketsAdapter(-1, documentNames, documentPathList,getContext());
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(),3);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                final String document = documentNames.get(position);
                System.out.println(document);
                // TODO mark as checked.
                /**
                Intent intent=new Intent(getContext(), OpenGallery.class);
                intent.putExtra("FROM","Images");
                startActivity(intent);
                 */
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        mAdapter.notifyDataSetChanged();
    }

    public void getDocumentBuckets(){
        final ContentResolver contentResolver = this.getContext().getContentResolver();
        final Uri uri = MediaStore.Files.getContentUri("external");

        final String[] projection = new String[] { MediaStore.Files.FileColumns.MIME_TYPE, MediaStore.Files.FileColumns.DISPLAY_NAME, MediaStore.Files.FileColumns.DATA };
        final String selection = MediaStore.Files.FileColumns.MEDIA_TYPE + "=? AND " + MediaStore.Files.FileColumns.MIME_TYPE + " IS NOT NULL";
        final String[] selectionArgs = new String[]{ String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_NONE) };
        final String sortOrder = MediaStore.Files.FileColumns.DATE_ADDED;

        final Cursor cursor = contentResolver.query(uri, projection, selection, selectionArgs, sortOrder);

        final ArrayList<String> documentNamesTEMP = new ArrayList<>(cursor.getCount());
        final ArrayList<String> documentPathListTEMP = new ArrayList<>(cursor.getCount());
        final HashSet<String> fileNames = new HashSet<>();
        File file;

        if (cursor.moveToLast()) {
            do {
                if (Thread.interrupted()) {
                    return;
                }

                final String mimeType = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.MIME_TYPE));
                if(isAllowed(mimeType)) {
                    final String fileName = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DISPLAY_NAME));
                    final String image = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA));

                    file = new File(image);
                    if (file.exists() && !fileNames.contains(fileName)) {
                        documentNamesTEMP.add(fileName);
                        documentPathListTEMP.add(image);
                        fileNames.add(fileName);
                    }
                }
            } while (cursor.moveToPrevious());
        }
        cursor.close();

        if (documentNamesTEMP == null) {
            documentNames = new ArrayList<>();
        }
        documentNames.clear();
        documentPathList.clear();
        documentNames.addAll(documentNamesTEMP);
        documentPathList.addAll(documentPathListTEMP);
    }

    /**
     *
     * @param mimeType
     * @return
     */
    private boolean isAllowed(String mimeType) {
        return mimeType != null && !mimeType.startsWith("image") && !mimeType.startsWith("audio") && !mimeType.startsWith("video");
    }

    public interface ClickListener {
        void onClick(View view, int position);
        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {
        private GestureDetector gestureDetector;
        private DocumentFragment.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final DocumentFragment.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }
                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

}



