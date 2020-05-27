package com.eldereach.eldereach.util;

import com.eldereach.eldereach.R;

import java.util.ArrayList;

public class EldereachHotline {
    private String name;
    private String description;
    private int image;
    private int hotline;

    public EldereachHotline(String name, String description, int image, int hotline) {
        this.name = name;
        this.description = description;
        this.image = image;
        this.hotline = hotline;
    }

    public static ArrayList<EldereachHotline> getHotlines() {
        ArrayList<EldereachHotline> list = new ArrayList<>();
        list.add(new EldereachHotline(
                "Alzheimer's Disease Association",
                "Striving towards a dementia inclusive society through our four strategic service pillars; Centre-Based Care, Caregiver Support, Academy and Community Enabling, the Association aims to advocate and inspire the society to regard and respect persons living with dementia as individuals who can still lead purposeful and meaningful lives.",
                R.drawable.ada,
                63770700
        ));

        list.add(new EldereachHotline(
                "Fei Yue Family Service Centre",
                "We believe in promoting social development in all ages, seek to inculcate the spirit of volunteerism in the community, and advocate strong family ties in the society. We are committed to serving and reaching out to the needy in Singapore through our diverse services and programmes catering to the needs of different client populations.",
                R.drawable.feiyue,
                68199177
        ));

        return list;
    }
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getImage() {
        return image;
    }

    public int getHotline() {
        return hotline;
    }
}
