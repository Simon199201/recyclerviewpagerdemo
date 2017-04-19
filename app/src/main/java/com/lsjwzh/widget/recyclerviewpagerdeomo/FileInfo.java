package com.lsjwzh.widget.recyclerviewpagerdeomo;

public class FileInfo {
    public long create_time;
    public long ModifiedDate;
    public String filePath;
    public long fileSize = 0;
    public int type; //详情请看 ScannerConst
    public String fileName;
    public int isDir; //0表示 文件夹 ,1 表示文件
    public boolean isSelected;
    public int Count;
    public String fileType;
    public String sortTime;  //YYYY-mm-DD

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isSelected() {
        return isSelected;
    }


    @Override
    public boolean equals(Object o) {
        return filePath.equals(((FileInfo) o).filePath);
    }

    @Override
    public String toString() {
        return "FileInfo{" +
                "create_time=" + create_time +
                ", ModifiedDate=" + ModifiedDate +
                ", filePath='" + filePath + '\'' +
                ", fileSize=" + fileSize +
                ", type=" + type +
                ", fileName='" + fileName + '\'' +
                ", isDir=" + isDir +
                ", isSelected=" + isSelected +
                ", Count=" + Count +
                ", fileType='" + fileType + '\'' +
                ", sortTime='" + sortTime + '\'' +
                '}';
    }
}