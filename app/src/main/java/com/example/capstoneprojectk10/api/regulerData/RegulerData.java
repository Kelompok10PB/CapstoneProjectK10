package com.example.capstoneprojectk10.api.regulerData;

import com.google.gson.annotations.SerializedName;

public class RegulerData {

    @SerializedName("data")
    private DerivativeData derivativeData;

    @SerializedName("update")
    private UpdatedData updatedData;

    public DerivativeData getDerivativeData() {
        return derivativeData;
    }

    public UpdatedData getUpdatedData() {
        return updatedData;
    }

    public static class DerivativeData {

        @SerializedName("jumlah_odp")
        private int mODP;

        @SerializedName("jumlah_pdp")
        private int mPDP;

        public int getmODP() {
            return mODP;
        }

        public int getmPDP() {
            return mPDP;
        }

    }

    public static class UpdatedData {

        @SerializedName("penambahan")
        private NewCases newCases;

        @SerializedName("total")
        private TotalCases totalCases;

        public NewCases getNewCases() {
            return newCases;
        }

        public TotalCases getTotalCases() {
            return totalCases;
        }

        public static class NewCases {

            @SerializedName("jumlah_positif")
            private int mPositif;

            @SerializedName("jumlah_meninggal")
            private int mMeninggal;

            @SerializedName("jumlah_sembuh")
            private int mSembuh;

            @SerializedName("jumlah_dirawat")
            private int mDirawat;

            @SerializedName("created")
            private String mWaktuUpdate;

            public int getmPositif() {
                return mPositif;
            }

            public int getmMeninggal() {
                return mMeninggal;
            }

            public int getmSembuh() {
                return mSembuh;
            }

            public String getmWaktuUpdate() {
                return mWaktuUpdate;
            }

        }

        public static class TotalCases {

            @SerializedName("jumlah_positif")
            private int mPositif;

            @SerializedName("jumlah_sembuh")
            private int mSembuh;

            @SerializedName("jumlah_meninggal")
            private int mMeninggal;

            public int getmPositif() {
                return mPositif;
            }

            public int getmSembuh() {
                return mSembuh;
            }

            public int getmMeninggal() {
                return mMeninggal;
            }
        }
        }
    }


