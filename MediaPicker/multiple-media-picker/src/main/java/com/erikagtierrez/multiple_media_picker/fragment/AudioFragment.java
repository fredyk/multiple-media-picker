package com.erikagtierrez.multiple_media_picker.fragment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.erikagtierrez.multiple_media_picker.OpenList;
import com.erikagtierrez.multiple_media_picker.adapter.BucketsAdapter;
import com.erikagtierrez.multiple_media_picker.R;
import com.erikagtierrez.multiple_media_picker.model.FileItem;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class AudioFragment extends Fragment{
    private static RecyclerView recyclerView;
    private BucketsAdapter mAdapter;
    private final String[] projection = new String[]{ MediaStore.Audio.Media.ALBUM, MediaStore.Audio.Media.DATA };
    private final String[] projection2 = new String[]{MediaStore.Audio.Media.DISPLAY_NAME,MediaStore.Audio.Media.DATA };
    private List<String> bucketNames= new ArrayList<>();
    private List<String> mp3List =new ArrayList<>();
    private static ArrayList<FileItem> audioList = new ArrayList<>();
    public static List<Boolean> selected=new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Bucket names reloaded
        mp3List.clear();
        audioList.clear();
        bucketNames.clear();
        getAlbumOfAudioFiles();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_grid, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        populateRecyclerView();
        return v;
    }

    private void populateRecyclerView() {
        mAdapter = new BucketsAdapter(bucketNames, mp3List,getContext());
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(),3);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                getAudioFiles(bucketNames.get(position));
                Intent intent=new Intent(getContext(), OpenList.class);
                intent.putExtra("data", audioList);
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        mAdapter.notifyDataSetChanged();
    }

    public void getAlbumOfAudioFiles(){

        Cursor cursor = getContext().getContentResolver()
                .query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection,
                        null, null, MediaStore.Audio.Media.DATE_ADDED);
        ArrayList<String> bucketNamesTEMP = new ArrayList<>(cursor.getCount());
        ArrayList<String> mp3ListTEMP = new ArrayList<>(cursor.getCount());
        HashSet<String> albumSet = new HashSet<>();
        File file;
        if (cursor.moveToLast()) {
            do {
                if (Thread.interrupted()) {
                    return;
                }
                String album = cursor.getString(cursor.getColumnIndex(projection[0]));
                String song = cursor.getString(cursor.getColumnIndex(projection[1]));
                file = new File(song);
                if (file.exists() && !albumSet.contains(album)) {
                    bucketNamesTEMP.add(album);
                    mp3ListTEMP.add(song);
                    albumSet.add(album);
                }
            } while (cursor.moveToPrevious());
        }
        cursor.close();
        if (bucketNamesTEMP == null) {
            bucketNames = new ArrayList<>();
        }
        bucketNames.clear();
        mp3List.clear();
        bucketNames.addAll(bucketNamesTEMP);
        mp3List.addAll(mp3ListTEMP);
    }

    public void getAudioFiles(String album){
        selected.clear();
        Cursor cursor = getContext().getContentResolver()
                .query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection2,
                         MediaStore.Audio.Media.ALBUM+" =?",new String[]{album},MediaStore.Images.Media.DATE_ADDED);
        ArrayList<FileItem> songsOfAlbum = new ArrayList<>(cursor.getCount());
        HashSet<String> fileSet = new HashSet<>();
        File file;
        if (cursor.moveToLast()) {
            do {
                if (Thread.interrupted()) {
                    return;
                }

                int image = R.drawable.ic_audio;
                String name = cursor.getString(cursor.getColumnIndex(projection2[0]));
                String path = cursor.getString(cursor.getColumnIndex(projection2[1]));
                file = new File(path);

                if (file.exists() && !fileSet.contains(path)) {
                    songsOfAlbum.add(new FileItem(image,name,path));
                    fileSet.add(path);
                    selected.add(false);
                }

            } while (cursor.moveToPrevious());
        }
        cursor.close();
        if (songsOfAlbum == null) {
            songsOfAlbum = new ArrayList<>();
        }
        audioList.clear();
        audioList.addAll(songsOfAlbum);
    }

    public interface ClickListener {
        void onClick(View view, int position);
        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {
        private GestureDetector gestureDetector;
        private AudioFragment.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final AudioFragment.ClickListener clickListener) {
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



