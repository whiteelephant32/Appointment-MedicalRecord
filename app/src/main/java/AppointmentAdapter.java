import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.edai.R;

import java.util.List;

public class AppointmentAdapter extends BaseAdapter {

    private Context context;
    private List<Appointment> appointmentList;

    public AppointmentAdapter(Context context, List<Appointment> appointmentList) {
        this.context = context;
        this.appointmentList = appointmentList;
    }

    @Override
    public int getCount() {
        return appointmentList.size();
    }

    @Override
    public Object getItem(int position) {
        return appointmentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_appointment, parent, false);
        }

        Appointment appointment = appointmentList.get(position);

        TextView tvName = convertView.findViewById(R.id.tvName);
        TextView tvDate = convertView.findViewById(R.id.tvDate);
        TextView tvTime = convertView.findViewById(R.id.tvTime);

        tvName.setText(appointment.getName());
        tvDate.setText(appointment.getDate());
        tvTime.setText(appointment.getTime());

        return convertView;
    }
}
