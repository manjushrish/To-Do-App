import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by manjushrish on 8/27/15.
 */
public class taskAdapter extends ArrayAdapter<Task> {

    private int layoutResourceId;
    public final static String TAG = "TaskAdapter";
    private LayoutInflater inflater;
    private List<Task> tasks;

    public taskAdapter(Context context, int layoutResourceId, List<Task> tasks) {
        super(context, layoutResourceId, tasks);
        this.layoutResourceId = layoutResourceId;
        this.tasks = tasks;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    static class taskHolder {
        TextView txtTitle;
        TextView txtShortDesc;
        TextView txtDate;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        taskHolder holder = null;

        if (null == convertView) {
            Log.d(TAG, "getView: rowView null: position " + position);
            convertView = inflater.inflate(layoutResourceId, parent, false);
            holder = new taskHolder();
            holder.txtTitle = (TextView) convertView.findViewById(R.id.title_row);
            holder.txtDate = (TextView) convertView.findViewById(R.id.date_row);
            holder.txtShortDesc = (TextView) convertView.findViewById(R.id.shortdesc_row);
            convertView.setTag(holder);
        } else {
            Log.d(TAG, "getView: rowView !null - reuse holder: position " +
                    position);
            holder = (taskHolder) convertView.getTag();
        }
        Log.d(TAG, " getView task " + tasks.size());
        try {
            Task task = tasks.get(position);
            holder.txtTitle.setText(task.getTitle());
            holder.txtShortDesc.setText(task.getShortd());
            holder.txtDate.setText(task.getDate());
            String com = getContext().getResources().getString(R.string.personal);

            if(task.getTag().equals(com))
            {
                holder.txtTitle.setTextColor(Color.rgb(255,255,0));
                holder.txtShortDesc.setTextColor(Color.rgb(255,255,0));
                holder.txtDate.setTextColor(Color.rgb(255,255,0));
            } else
            {

                holder.txtTitle.setTextColor(Color.rgb(255,158,55));
                holder.txtShortDesc.setTextColor(Color.rgb(255,158,55));
                holder.txtDate.setTextColor(Color.rgb(255,158,55));
            }

        } catch (Exception e) {
            Log.e(TAG, " getView task " + e + " position was : " + position +
                    " task.size: " + tasks.size());
        }
        return convertView;
    }


}
