#日期搜索控件
![github](https://raw.githubusercontent.com/hunimeizi/DateSearchControl/8b16c41609692ea721f5ccb074fc653ae5ed1ed3/app/src/main/res/mipmap-hdpi/searchview.png "github")</br>
#####利用DialogFragment实现调用系统日历控件
            /**
             * 日期选择框
             */
            public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
                private OnDateSelectedListener listener;
                private int mYear, mMonth, mDay;

                @Override
                public Dialog onCreateDialog(Bundle savedInstanceState) {
                    // 根据传入的参数设置初始日期
                    Bundle bundle = getArguments();
                    if (bundle != null) {
                        mYear = bundle.getInt("year", 0);
                        mMonth = bundle.getInt("month", 0);
                        mDay = bundle.getInt("day", 0);
                    }
                    // 设置当前时间为初始日期
                    if (mYear == 0 || mMonth == 0 || mDay == 0) {
                        Calendar c = Calendar.getInstance();
                        mYear = c.get(Calendar.YEAR);
                        mMonth = c.get(Calendar.MONTH);
                        mDay = c.get(Calendar.DAY_OF_MONTH);
                    }
                    return new DatePickerDialog(getActivity(), this, mYear, mMonth, mDay);
                }

                public void onDateSet(DatePicker view, int year, int month, int day) {
                    if (listener != null) {
                        listener.onDateSelected(year, month, day);
                    }
                }

                public interface OnDateSelectedListener {
                    void onDateSelected(int year, int month, int day);
                }

                public void setDateSelectedListener(OnDateSelectedListener listener) {
                    this.listener = listener;
                }
            }
#####根据日期搜索控件分为开始日期和结束日期，且结束日期不能小于开始日期，为了实现方便重新封装了一个view（SearchByDateView）暴露出一个接口进行直接调用可以获取开始日期和结束日期时间
             startDateTv.setOnClickListener(new OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     DatePickerFragment datePickerFragment = new DatePickerFragment();
                     int year = 0, month = 0, day = 0;
                     if (!TextUtils.isEmpty(startDate)) {
                         String[] strings = startDate.split("-");
                         if (strings.length == 3) {
                             year = Integer.parseInt(strings[0]);
                             month = Integer.parseInt(strings[1]) - 1;
                             day = Integer.parseInt(strings[2]);
                         }
                     }
                     Bundle bundle = new Bundle();
                     bundle.putInt("year", year);
                     bundle.putInt("month", month);
                     bundle.putInt("day", day);
                     datePickerFragment.setArguments(bundle);
                     datePickerFragment.setDateSelectedListener(new StartDateSelectedListener());
                     datePickerFragment.show(((Activity) context).getFragmentManager(), "Dialog");
                 }
             });
             endDateTv.setOnClickListener(new OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     DatePickerFragment datePickerFragment = new DatePickerFragment();
                     int year = 0, month = 0, day = 0;
                     if (!TextUtils.isEmpty(endDate)) {
                         String[] strings = endDate.split("-");
                         if (strings.length == 3) {
                             year = Integer.parseInt(strings[0]);
                             month = Integer.parseInt(strings[1]) - 1;
                             day = Integer.parseInt(strings[2]);
                         }
                     }
                     Bundle bundle = new Bundle();
                     bundle.putInt("year", year);
                     bundle.putInt("month", month);
                     bundle.putInt("day", day);
                     datePickerFragment.setArguments(bundle);
                     datePickerFragment.setDateSelectedListener(new EndDateSelectedListener());
                     datePickerFragment.show(((Activity) context).getFragmentManager(), "Dialog");
                 }
             });
######自己写个接口按钮搜索时调用
             public void setOnSearchListener(OnSearchListener onSearchListener) {
                                this.onSearchListener = onSearchListener;
             }

             public interface OnSearchListener {
                           void onSearch(String startDate, String endDate, String username);
             }
######布局中
            <com.control.search.date.view.SearchByDateView
                             android:id="@+id/activity_date_search_control_searchByDateView"
                             android:layout_width="match_parent"
                             android:layout_height="wrap_content" />
#####根据自己编写的接口直接利用该对象实现接口便可获取到相应的日历时间
        SearchByDateView activity_date_search_control_searchByDateView = (SearchByDateView) findViewById(R.id.activity_date_search_control_searchByDateView);
        activity_date_search_control_searchByDateView.setOnSearchListener(new SearchByDateView.OnSearchListener() {
            @Override
            public void onSearch(String startDate, String endDate, String username) {
                Toast.makeText(DateSearchControlActivity.this, "开始日期为：" + startDate + "   结束日期为：" + endDate, Toast.LENGTH_SHORT).show();
            }
        });