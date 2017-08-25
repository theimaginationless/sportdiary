package com.app.dmitryteplyakov.sportdiary.Timer;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.dmitryteplyakov.sportdiary.Core.TimerTemplate.TimerTemplate;
import com.app.dmitryteplyakov.sportdiary.Core.TimerTemplate.TimerTemplateStorage;
import com.app.dmitryteplyakov.sportdiary.Dialogs.DeleteFragment;
import com.app.dmitryteplyakov.sportdiary.Dialogs.TitlePickerFragment;
import com.app.dmitryteplyakov.sportdiary.R;

import java.util.List;
import java.util.UUID;

/**
 * Created by dmitry21 on 19.08.17.
 */

public class TimerTemplatesListFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private TimerAdapter mAdapter;
    private TextView mEmptyTextView;
    private FloatingActionButton mFab;
    public static final int REQUEST_NEW_TEMPLATE = 19;
    public static final int REQUEST_EDIT_TEMPLATE = 20;
    public static final int REQUEST_DELETE_TEMPLATE = 21;
    private static final String DIALOG_TEMPLATE_TITLE = "com.app.timertemplateslistfragment.dialog_template_timer";
    private static final String DIALOG_TEMPLATE_DELETE = "com.app.timertemplateslistfragment.dialog_template_delete";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list_programs, container, false);
        mFab = (FloatingActionButton) v.findViewById(R.id.fragment_list_programs_add_program_action_fab);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.fragment_list_programs_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mEmptyTextView = (TextView) v.findViewById(R.id.fragment_list_programs_empty_text);
        mEmptyTextView.setText(getString(R.string.timer_templates_empty));
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if(dy > 0)
                    mFab.hide();
                else if(dy < 0)
                    mFab.show();
            }
        });

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                //Timer timer = new Timer();
                //TimerStorage.get(getActivity()).addTimer(timer);
                TitlePickerFragment dialog = TitlePickerFragment.newInstance(UUID.randomUUID());
                dialog.setTargetFragment(TimerTemplatesListFragment.this, REQUEST_NEW_TEMPLATE);
                dialog.show(manager, DIALOG_TEMPLATE_TITLE);
            }
        });
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateUI();
            }
        });
        return v;
    }

    private class TimerTemplateHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private TimerTemplate mTimerTemplate;
        private TextView mTitle;
        private CharSequence[] options;

        public TimerTemplateHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            options = new CharSequence[]{getString(R.string.edit_training_title_menu), getString(R.string.menu_delete_item)};
            mTitle = itemView.findViewById(R.id.group_name);
        }

        public void bindTimer(TimerTemplate template) {
            mTimerTemplate = template;
            mTitle.setText(mTimerTemplate.getTitle());
        }

        @Override
        public void onClick(View v) {
            Intent intent = TimerListActivity.newInstance(getActivity(), mTimerTemplate.getId());
            startActivity(intent);
        }

        @Override
        public boolean onLongClick(View v) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
            dialog.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (i == 0) {
                        FragmentManager manager = getFragmentManager();
                        TitlePickerFragment dialog = TitlePickerFragment.newInstance(mTimerTemplate.getId());
                        dialog.setTargetFragment(TimerTemplatesListFragment.this, REQUEST_EDIT_TEMPLATE);
                        dialog.show(manager, DIALOG_TEMPLATE_TITLE);
                    } else if (i == 1) {
                        FragmentManager manager = getFragmentManager();
                        DeleteFragment dialog = DeleteFragment.newInstance(mTimerTemplate.getId());
                        dialog.setTargetFragment(TimerTemplatesListFragment.this, REQUEST_DELETE_TEMPLATE);
                        dialog.show(manager, DIALOG_TEMPLATE_DELETE);
                    }
                }
            });
            dialog.show();
            return true;
        }
    }

    private class TimerAdapter extends RecyclerView.Adapter<TimerTemplateHolder> {
        private List<TimerTemplate> mTimerTemplates;

        public TimerAdapter(List<TimerTemplate> templates) {
            mTimerTemplates = templates;
        }

        @Override
        public TimerTemplateHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.programs_list_item, parent, false);
            return new TimerTemplateHolder(view);
        }

        @Override
        public void onBindViewHolder(TimerTemplateHolder holder, int position) {
            TimerTemplate template = mTimerTemplates.get(position);
            holder.bindTimer(template);
        }

        @Override
        public int getItemCount() {
            return mTimerTemplates.size();
        }

        public void setTimers(List<TimerTemplate> templates) {
            mTimerTemplates = templates;
        }
    }

    private void updateUI() {
        List<TimerTemplate> templates = TimerTemplateStorage.get(getActivity()).getTemplates();
        if(mAdapter == null) {
            mAdapter = new TimerAdapter(templates);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setTimers(templates);
            mAdapter.notifyDataSetChanged();
        }
        if(templates.size() != 0) {
            mEmptyTextView.setVisibility(View.GONE);
        } else
            mEmptyTextView.setVisibility(View.VISIBLE);
    }

    private void updateUI(boolean isAdd, int num) {
        List<TimerTemplate> templates = TimerTemplateStorage.get(getActivity()).getTemplates();
        mAdapter.setTimers(templates);
        if(isAdd) {
            mAdapter.notifyItemInserted(num);
        } else
            mAdapter.notifyItemRemoved(num);
        if(templates.size() != 0) {
            mEmptyTextView.setVisibility(View.GONE);
        } else
            mEmptyTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK) {
            if(requestCode == REQUEST_NEW_TEMPLATE) {
                TimerTemplate template = new TimerTemplate();
                template.setTitle((String) data.getSerializableExtra(TitlePickerFragment.EXTRA_TITLE));
                template.setId((UUID) data.getSerializableExtra(TitlePickerFragment.EXTRA_NEW_UUID));
                TimerTemplateStorage.get(getActivity()).addTemplate(template);
                final int num = TimerTemplateStorage.get(getActivity()).getTemplates().indexOf(template);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateUI(true, num);
                    }
                });
            } else if(requestCode == REQUEST_EDIT_TEMPLATE) {
                TimerTemplate template = new TimerTemplate();
                template.setTitle((String) data.getSerializableExtra(TitlePickerFragment.EXTRA_TITLE));
                template.setId((UUID) data.getSerializableExtra(TitlePickerFragment.EXTRA_NEW_UUID));
                TimerTemplateStorage.get(getActivity()).updateTemplate(template);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateUI();
                    }
                });
            } else if(requestCode == REQUEST_DELETE_TEMPLATE) {
                TimerTemplate template = TimerTemplateStorage.get(getActivity()).getTemplate((UUID) data.getSerializableExtra(DeleteFragment.EXTRA_RETURN_DELETE_UUID));
                final int num = TimerTemplateStorage.get(getActivity()).getTemplates().indexOf(template);
                TimerTemplateStorage.get(getActivity()).deleteTemplate(template);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateUI(false, num);
                    }
                });
            }
        }
    }
}
