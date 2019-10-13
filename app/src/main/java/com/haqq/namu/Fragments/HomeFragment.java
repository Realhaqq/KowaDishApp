package com.haqq.namu.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
//import com.haqq.namu.Activities.FoodActivity;
import com.haqq.namu.Activities.FoodDetailActivity;
import com.haqq.namu.Activities.FoodListActivity;
import com.haqq.namu.Activities.FoodSearchActivity;
import com.haqq.namu.Models.DiscountItem;
import com.haqq.namu.Models.Item;
import com.haqq.namu.Others.CustomItemAdapter;
import com.haqq.namu.Others.CustomPagerAdapter;
import com.haqq.namu.Others.DiscountItemAdapter;
import com.haqq.namu.Others.Pager;
import com.haqq.namu.Others.PrefManager;
import com.haqq.namu.R;
import com.haqq.namu.users.SessionHandler;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by sagar on 28/6/17.
 */

public class HomeFragment extends Fragment implements ViewPager.OnPageChangeListener, View.OnClickListener {

    private View view;
    private Pager mViewPager;
    private CustomPagerAdapter mAdapter;
    private LinearLayout pager_indicator;
    private int dotsCount;
    private ImageView[] dots;
    private RecyclerView horizontal_recycler_view;
    SessionHandler session;
    private ImageView btn_search;
    private EditText et_search;


    private DiscountItemAdapter adapter;
    private RecyclerView recyclerView;
    private List<DiscountItem> itemList;

    private LinearLayout linearLayout;
    private TextView city;
    private TextView street;
    private FloatingActionButton fab_pizza, fab_meal, fab_burger, fab_shawarma, fab_chips, fab_chinese, fab_chicken, fab_samosa, fab_mocktail;

    private final int[] item = {R.drawable.pizza,
            R.drawable.main_course,
            R.drawable.burger,
            R.drawable.chinese,
            R.drawable.soup};

    private final int PLACE_PICKER_REQUEST = 1;
    private PrefManager pref;

    private final int[] mResources = {R.drawable.banerburger, R.drawable.banerburger, R.drawable.banerburger, R.drawable.banerburger};


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_home, container, false);
        mViewPager = (Pager) view.findViewById(R.id.viewpager);
        pager_indicator = (LinearLayout) view.findViewById(R.id.viewPagerCountDots);
        linearLayout = (LinearLayout) view.findViewById(R.id.layout_search);
        linearLayout.setOnClickListener(this);
        session = new SessionHandler(getContext());

        city = (TextView) view.findViewById(R.id.city);
        street = (TextView) view.findViewById(R.id.street);
        pref = new PrefManager(getActivity());


        fab_pizza = view.findViewById(R.id.pizza);
        fab_burger = view.findViewById(R.id.burger);
        fab_chicken = view.findViewById(R.id.chicken);
        fab_chinese = view.findViewById(R.id.chinese);
        fab_chips = view.findViewById(R.id.chips);
        fab_meal = view.findViewById(R.id.meal);
        fab_mocktail = view.findViewById(R.id.mocktail);
        fab_samosa = view.findViewById(R.id.samosa);
        fab_shawarma = view.findViewById(R.id.shawarma);
        fab_pizza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FoodListActivity.class);
                intent.putExtra("filter", "pizza");
                startActivity(intent);
            }
        });
        fab_burger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FoodListActivity.class);
                intent.putExtra("filter", "burger");
                startActivity(intent);
            }
        });
        fab_chicken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FoodListActivity.class);
                intent.putExtra("filter", "chicken");
                startActivity(intent);
            }
        });
        fab_chinese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FoodListActivity.class);
                intent.putExtra("filter", "chinese");
                startActivity(intent);
            }
        });
        fab_chips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FoodListActivity.class);
                intent.putExtra("filter", "chips");
                startActivity(intent);
            }
        });
        fab_meal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FoodListActivity.class);
                intent.putExtra("filter", "meal");
                startActivity(intent);
            }
        });
        fab_mocktail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FoodListActivity.class);
                intent.putExtra("filter", "mocktail");
                startActivity(intent);
            }
        });
        fab_samosa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FoodListActivity.class);
                intent.putExtra("filter", "samosa");
                startActivity(intent);
            }
        });
        fab_shawarma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FoodListActivity.class);
                intent.putExtra("filter", "shawarma");
                startActivity(intent);
            }
        });


        //setting the adapter the image's viewpager
        mAdapter = new CustomPagerAdapter(getActivity(), mResources);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(0);
        mViewPager.addOnPageChangeListener(this);
        setPageViewIndicator();

        btn_search = view.findViewById(R.id.btn_search);
        et_search = view.findViewById(R.id.etsearch);

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String filter = et_search.getText().toString().trim();
                Intent intent = new Intent(getContext(), FoodSearchActivity.class);
                intent.putExtra("filter", filter);
                startActivity(intent);
            }
        });

        return view;
    }

    private void setPageViewIndicator() {

        //setting the dots for image's view pager

        Log.d("###setPageViewIndicator", " : called");
        dotsCount = mAdapter.getCount();
        dots = new ImageView[dotsCount];

        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(getActivity());
            dots[i].setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.nonselected_item_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(4, 0, 4, 0);

            final int presentPosition = i;
            dots[presentPosition].setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    mViewPager.setCurrentItem(presentPosition);
                    return true;
                }

            });


            pager_indicator.addView(dots[i], params);
        }

        dots[0].setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.selected_item_dot));
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

        Log.d("###onPageSelected, pos ", String.valueOf(position));
        for (int i = 0; i < dotsCount; i++) {
            dots[i].setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.nonselected_item_dot));
        }

        dots[position].setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.selected_item_dot));

        if (position + 1 == dotsCount) {

        } else {

        }


    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

//
//    private void setAdapter() {
//
//        ArrayList<Item> horizontalList = new ArrayList<>();
//
//        //filling the data in horizontal recycle view of home page
//        horizontalList.add(new Item(item[0], "Pizza"));
//        horizontalList.add(new Item(item[1], "Meal Combo"));
//        horizontalList.add(new Item(item[2], "Burger"));
//        horizontalList.add(new Item(item[3], "Chinese"));
//        horizontalList.add(new Item(item[4], "Soup"));
//        horizontalList.add(new Item(item[0], "Pizza"));
//        horizontalList.add(new Item(item[1], "Meal Combo"));
//        horizontalList.add(new Item(item[2], "Burger"));
//        horizontalList.add(new Item(item[3], "Chinese"));
//        horizontalList.add(new Item(item[4], "Soup"));
//
//        CustomItemAdapter horizontalAdapter = new CustomItemAdapter(getActivity(), horizontalList, new CustomItemAdapter.VenueAdapterClickCallbacks() {
//            @Override
//            public void onCardClick(String p) {
//                pref.setItem(p);
//                startActivity(new Intent(getActivity(), FoodListActivity.class));
//            }
//        });
//
//        horizontal_recycler_view.setAdapter(horizontalAdapter);
//    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {

            case R.id.layout_search:
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                try {
                    startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(getContext(), data);
//                String toastMsg = String.format("Place: %s", place.getName());
                String add[] = place.getAddress().toString().split(",");
                if (add.length >= 4) {
                    city.setText(add[add.length - 3]);
                    String str = add[add.length - 4];
                    if (str.length() >= 15) {
                        street.setText(" " + add[add.length - 4].substring(0, 15) + ".");
                        session.SetDefaultLocation(place.getAddress().toString());
                    }else
                        street.setText(" " + add[add.length - 4]);
                    session.SetDefaultLocation(place.getAddress().toString());

                } else {
                    city.setText(place.getName());
                    street.setText(place.getName());
                }
            }
        }
    }
}
