package com.pixelplex.qtum.ui.fragment.BackupContractsFragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ShareCompat;

import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentView;
import com.pixelplex.qtum.ui.fragment.OpenFileDialog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by max-v on 6/15/2017.
 */

public class BackupContractsFragmentPresenter extends BaseFragmentPresenterImpl {

    BackupContractsFragmentView mBackupContractsFragmentView;

    public BackupContractsFragmentPresenter(BackupContractsFragmentView backupContractsFragmentView){
        mBackupContractsFragmentView = backupContractsFragmentView;
    }

    @Override
    public BackupContractsFragmentView getView() {
        return mBackupContractsFragmentView;
    }

    public void onBackUpClick(){

        String path = getView().getContext().getFilesDir().getPath()+ "/tmp";
        File targetFile = new File(path);
        Intent intentShareFile = new Intent(Intent.ACTION_SEND);


        String dstPath = Environment.getExternalStorageDirectory() + File.separator + "myApp" + File.separator;
        File dst = new File(dstPath);

        try {
            exportFile(targetFile, dst);
        } catch (IOException e) {
            e.printStackTrace();
        }


        if(dst.exists()) {
            intentShareFile.setType("application/txt");
            intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+dstPath+File.separator + "tmp" + ".txt"));

            intentShareFile.putExtra(Intent.EXTRA_SUBJECT,
                    "Sharing File...");
            intentShareFile.putExtra(Intent.EXTRA_TEXT, "Sharing File...");

            getView().getMainActivity().startActivityForResult(Intent.createChooser(intentShareFile, "Share File"),222);
        }
        dst.delete();
    }

    private File exportFile(File src, File dst) throws IOException {

        //if folder does not exist
        if (!dst.exists()) {
            if (!dst.mkdir()) {
                return null;
            }
        }

        File expFile = new File(dst.getPath() + File.separator + "tmp" + ".txt");
        FileChannel inChannel = null;
        FileChannel outChannel = null;

        try {
            inChannel = new FileInputStream(src).getChannel();
            outChannel = new FileOutputStream(expFile).getChannel();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } finally {
            if (inChannel != null)
                inChannel.close();
            if (outChannel != null)
                outChannel.close();
        }

        return expFile;
    }

}
