package com.xiachen.fatter;

import android.os.Parcel;
import android.os.Parcelable;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.Date;

/**
 * Created by xiachen on 15/12/6.
 */
@Table(name = "Bean")
public class Bean extends Model implements Parcelable{
    @Column(name = "Weight")
    public float weight;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Column(name = "Date")
    public String date;

    @Column(name = "Snack")
    public boolean ifSnack;

    @Column(name = "Bath")
    public boolean ifBath;

    @Column(name = "Defecation")
    public boolean ifDefecate;


    public Bean(float weight, boolean ifSnack, boolean ifBath, boolean ifDefecate) {
        this.weight = weight;
        this.ifSnack = ifSnack;
        this.ifBath = ifBath;
        this.ifDefecate = ifDefecate;
    }

    public Bean() {
        super();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(this.weight);
        dest.writeString(this.date);
        dest.writeByte(ifSnack ? (byte) 1 : (byte) 0);
        dest.writeByte(ifBath ? (byte) 1 : (byte) 0);
        dest.writeByte(ifDefecate ? (byte) 1 : (byte) 0);
    }

    protected Bean(Parcel in) {
        this.weight = in.readFloat();
        this.date = in.readString();
        this.ifSnack = in.readByte() != 0;
        this.ifBath = in.readByte() != 0;
        this.ifDefecate = in.readByte() != 0;
    }

    public static final Creator<Bean> CREATOR = new Creator<Bean>() {
        public Bean createFromParcel(Parcel source) {
            return new Bean(source);
        }

        public Bean[] newArray(int size) {
            return new Bean[size];
        }
    };
}
