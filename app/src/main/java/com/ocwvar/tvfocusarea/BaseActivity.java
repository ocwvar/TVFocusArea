package com.ocwvar.tvfocusarea;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class BaseActivity extends AppCompatActivity {

	private LHTestAdapter lhAdapter;
	private LVTestAdapter lvAdapter;
	private GTestAdapter gAdapter;

	public LHTestAdapter getLHAdapter() {
		if ( this.lhAdapter == null ) {
			this.lhAdapter = new LHTestAdapter();
		}
		return lhAdapter;
	}

	public LVTestAdapter getLVAdapter() {
		if ( this.lvAdapter == null ) {
			this.lvAdapter = new LVTestAdapter();
		}
		return lvAdapter;
	}

	public GTestAdapter getGAdapter() {
		if ( this.gAdapter == null ) {
			this.gAdapter = new GTestAdapter();
		}
		return gAdapter;
	}

	//横向线性列表适配器
	private class LHTestAdapter extends RecyclerView.Adapter {

		@NonNull
		@Override
		public RecyclerView.ViewHolder onCreateViewHolder( @NonNull ViewGroup parent, int viewType ) {
			return new ViewHolder( LayoutInflater.from( parent.getContext() ).inflate( R.layout.item_h_text, parent, false ) );
		}

		@Override
		public void onBindViewHolder( @NonNull RecyclerView.ViewHolder holder, int position ) {
			ViewHolder viewHolder = (ViewHolder) holder;
			viewHolder.item.setText( String.valueOf( position + 1 ) );
		}

		@Override
		public int getItemCount() {
			return 50;
		}

		private class ViewHolder extends RecyclerView.ViewHolder {

			TextView item;

			ViewHolder( View itemView ) {
				super( itemView );
				item = (TextView) itemView;
			}
		}

	}

	//竖向线性列表适配器
	private class LVTestAdapter extends RecyclerView.Adapter {

		@NonNull
		@Override
		public RecyclerView.ViewHolder onCreateViewHolder( @NonNull ViewGroup parent, int viewType ) {
			return new ViewHolder( LayoutInflater.from( parent.getContext() ).inflate( R.layout.item_v_text, parent, false ) );
		}

		@Override
		public void onBindViewHolder( @NonNull RecyclerView.ViewHolder holder, int position ) {
			ViewHolder viewHolder = (ViewHolder) holder;
			viewHolder.item.setText( String.valueOf( position + 1 ) );
		}

		@Override
		public int getItemCount() {
			return 50;
		}

		private class ViewHolder extends RecyclerView.ViewHolder {

			TextView item;

			ViewHolder( View itemView ) {
				super( itemView );
				item = (TextView) itemView;
			}
		}

	}

	//表格列表适配器
	private class GTestAdapter extends RecyclerView.Adapter {

		@NonNull
		@Override
		public RecyclerView.ViewHolder onCreateViewHolder( @NonNull ViewGroup parent, int viewType ) {
			return new ViewHolder( LayoutInflater.from( parent.getContext() ).inflate( R.layout.item_g_text, parent, false ) );
		}

		@Override
		public void onBindViewHolder( @NonNull RecyclerView.ViewHolder holder, int position ) {
			ViewHolder viewHolder = (ViewHolder) holder;
			viewHolder.item.setText( String.valueOf( position + 1 ) );
		}

		@Override
		public int getItemCount() {
			return 50;
		}

		private class ViewHolder extends RecyclerView.ViewHolder {

			TextView item;

			ViewHolder( View itemView ) {
				super( itemView );
				item = (TextView) itemView;
			}
		}

	}

}
