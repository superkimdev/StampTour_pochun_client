package com.thatzit.kjw.stamptour_kyj_client.checker;

import android.os.Environment;
import android.os.StatFs;

import com.thatzit.kjw.stamptour_kyj_client.checker.action.Check_External_memory;

import java.io.File;

/**
 * Created by kjw on 16. 9. 19..
 */
public class UsableStorageChecker implements Check_External_memory{
    private final String ERROR = "-1";

    public UsableStorageChecker() {
    }

    public boolean externalMemoryAvailable() {
        return android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);
    }

    public String getAvailableInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return formatSize(availableBlocks * blockSize);
    }

    public String getTotalInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return formatSize(totalBlocks * blockSize);
    }

    public PreCheckSizeDTO getAvailableExternalMemorySize() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();
            long size = blockSize*availableBlocks;
            String nonformatting = size+"";
            PreCheckSizeDTO data = new PreCheckSizeDTO(nonformatting,formatSize(size));
            return data;
        } else {
            return new PreCheckSizeDTO("-1","-1");
        }
    }

    public PreCheckSizeDTO getTotalExternalMemorySize() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long totalBlocks = stat.getBlockCount();
            long size = blockSize*totalBlocks;
            String nonformatting = size+"";
            PreCheckSizeDTO data = new PreCheckSizeDTO(nonformatting,formatSize(size));
            return data;
        } else {
            return new PreCheckSizeDTO("-1","-1");
        }
    }

    public String formatSize(long size) {
        String suffix = null;

        if (size >= 1024) {
            suffix = "KB";
            size /= 1024;
            if (size >= 1024) {
                suffix = "MB";
                size /= 1024;
            }
        }

        StringBuilder resultBuffer = new StringBuilder(Long.toString(size));

        int commaOffset = resultBuffer.length() - 3;
        while (commaOffset > 0) {
            resultBuffer.insert(commaOffset, ',');
            commaOffset -= 3;
        }

        if (suffix != null) resultBuffer.append(suffix);
        return resultBuffer.toString();
    }

    @Override
    public ExternalMemoryDTO check_ext_memory() {
        PreCheckSizeDTO usable_size = getAvailableExternalMemorySize();
        PreCheckSizeDTO total_size = getTotalExternalMemorySize();
        if(usable_size.getNonformatting().equals("-1")){
            return new ExternalMemoryDTO(ERROR,ERROR,ERROR,ERROR);
        }else{
            return new ExternalMemoryDTO(usable_size.getFormatting(),usable_size.getNonformatting(),
                    total_size.getFormatting(),total_size.getNonformatting());
        }
    }
}
