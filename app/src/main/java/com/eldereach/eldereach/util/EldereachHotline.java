package com.eldereach.eldereach.util;

import com.eldereach.eldereach.R;

import java.util.ArrayList;

public class EldereachHotline {
    private String name;
    private String description;
    private int image;
    private String hotline;

    private EldereachHotline(String name, String description, int image, String hotline) {
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
                "63770700"
        ));

        list.add(new EldereachHotline(
                "Fei Yue Family Service Centre",
                "We believe in promoting social development in all ages, seek to inculcate the spirit of volunteerism in the community, and advocate strong family ties in the society. We are committed to serving and reaching out to the needy in Singapore through our diverse services and programmes catering to the needs of different client populations.",
                R.drawable.feiyue,
                "68199177"
        ));

        list.add(new EldereachHotline(
                "Food from the Heart",
                "To be the leading charity in Singapore devoted to alleviating hunger through efficient distribution of food. To reach out to the less-fortunate and brighten their lives by alleviating hunger through a food distribution programme and bringing joy through the distribution of toys and birthday celebrations.",
                R.drawable.food_from_the_heart,
                "62804483"
        ));

        list.add(new EldereachHotline(
                "Lions Befrienders",
                "Lions Befrienders Service Association (Singapore), a Social Service Agency, was formed in 1995 by the Lions Clubs of Singapore and a Lions District Project. Our mission is to provide friendship and care for seniors to age in place with community participation, enabling them to enjoy meaningful and enriching lives.",
                R.drawable.lions_befrienders,
                "18003758600"
        ));

        list.add(new EldereachHotline(
                "Habitat for Humanity",
                "At Habitat for Humanity Singapore, we build decent homes across Asia-Pacific and rehabilitate flats in Singapore for vulnerable families. Habitat Singapore began operations in 2004 and has been actively engaging volunteers to work alongside us in fighting poverty housing in Singapore and across the Asia-Pacific region.",
                R.drawable.habitat_for_humanity,
                "67447326"
        ));

        list.add(new EldereachHotline(
                "HCA Hospice Care",
                "HCA Hospice Care (HCA) is Singapore's largest home hospice care provider and is a registered charity since 1989. The organisation provides comfort and support to patients with life-limiting illnesses regardless of age, religion, ethnicity, nationality and financial status.",
                R.drawable.hca,
                "62512561"
        ));

        list.add(new EldereachHotline(
                "Red Cross",
                "Singapore Red Cross is dedicated to relieving human suffering, protecting human lives and dignity and responding to emergencies.",
                R.drawable.red_cross,
                "66640500"
        ));

        list.add(new EldereachHotline(
                "Silver Ribbon",
                "To combat mental health stigma, encourage early help, and facilitate integration of people with mental illness within the society through innovative means of promoting mental health literacy.",
                R.drawable.silver_ribbon,
                "63861928"
        ));

        list.add(new EldereachHotline(
                "The Salvation Army",
                "The Salvation Army has been touching lives in Singapore since 1935 and we remain committed in serving the underprivileged in the community without discrimination. To us, every life is precious and we actively help those in need regardless of age, race or religion.",
                R.drawable.the_salvation_army,
                "65550188"
        ));

        list.add(new EldereachHotline(
                "TOUCH Community Services",
                "Over the years, TOUCH has reached out to many individuals from all religions and races, including children, youths, families, people with special and healthcare needs and the elderly.",
                R.drawable.touch,
                "63770122"
        ));

        list.add(new EldereachHotline(
                "Willing Hearts",
                "A secular, non-affiliated charity, Willing Hearts is wholly run by volunteers, apart from a handful of staff. It operates a soup kitchen that prepares, cooks and distributes about 6,500 daily meals to over 40 locations island wide, 365 days a year.",
                R.drawable.willing_hearts,
                "64765822"
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

    public String getHotline() {
        return hotline;
    }
}
