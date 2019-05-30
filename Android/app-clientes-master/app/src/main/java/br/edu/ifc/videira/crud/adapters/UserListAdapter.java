package br.edu.ifc.videira.crud.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.edu.ifc.videira.crud.R;
import br.edu.ifc.videira.crud.entities.User;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserViewHolder> {

    class UserViewHolder extends RecyclerView.ViewHolder {
        private final TextView userItemView;

        private UserViewHolder(View itemView) {
            super(itemView);
            userItemView = itemView.findViewById(R.id.textView);
        }
    }

    private final LayoutInflater mInflater;
    private List<User> mUsers; // Cached copy of users

    public UserListAdapter(Context context) { mInflater = LayoutInflater.from(context); }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new UserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        if (mUsers != null) {
            User current = mUsers.get(position);
            holder.userItemView.setText(current.getEmail());
        } else {
            // Covers the case of data not being ready yet.
            holder.userItemView.setText("No Email yet");
        }
    }

    public void setUsers(List<User> users){
        mUsers = users;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mUsers has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (mUsers != null)
            return mUsers.size();
        else return 0;
    }

}
