package com.example.e_padhai;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;



import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class feedAdapter extends RecyclerView.Adapter<feedAdapter.feedViewAdapter> {
    private List<feedData> list;
    private Context context;

    public feedAdapter(List<feedData> list, Context context) {
        this.list = list;
        this.context = context;
    }


    @NonNull
    @NotNull
    @Override
    public feedViewAdapter onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.feed_item_layout,parent,false);

        return new feedViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull feedViewAdapter holder, int position) {


        feedData item=list.get(position);

        holder.listItems = new ArrayList<NumbersView>();
        holder.authName.setText(item.getAuthor());
        holder.date.setText(item.getDate()+" "+item.getTime());
        holder.content.setText(item.getText());

        holder.content.setMovementMethod(LinkMovementMethod.getInstance());
        holder.listView.setOnTouchListener((v, event) -> {
            int action = event.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    // Disallow ScrollView to intercept touch events.
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    break;

                case MotionEvent.ACTION_UP:
                    // Allow ScrollView to intercept touch events.
                    v.getParent().requestDisallowInterceptTouchEvent(false);
                    break;
            }

            // Handle ListView touch events.
            v.onTouchEvent(event);
            return true;
        });

        List<String> urls=item.getUrlList();

        if(urls!=null) {
            int fileNo = 1;
            String fileName = "default";
            holder.adapter = new NumbersViewAdapter(context, holder.listItems);
            holder.listView.setAdapter(holder.adapter);
            holder.listView.setOnItemClickListener((a, v, pos, id) ->
            {


                Object selectedItem = a.getItemAtPosition(pos);

                NumbersView nv = (NumbersView) selectedItem;

                Uri uri = nv.geturi();

                // String uri=urls.get(Integer.parseInt(nv.getNumberInDigit()) - 1);

                if (getMimeType(context, uri).equals("pdf")) {
              /* Intent browserIntent = new Intent(Intent.ACTION_VIEW,Uri.parse(uri));
                context.startActivity(browserIntent);*/


                    Intent intent1 = new Intent(context, pdfViewer.class);
                    intent1.putExtra("url", uri.toString());

                    context.startActivity(intent1);
                } else if (getMimeType(context, uri).equals("jpg")) {
                    Intent intent = new Intent(context, FullPage.class);
                    intent.putExtra("image", uri.toString());
                    v.getContext().startActivity(intent);
                } else {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, uri);
                    context.startActivity(browserIntent);
                }


            });
            for (String url : urls) {
                Uri uri = Uri.parse(url);
                int s = url.indexOf("%2F-") + 4;
                int f = url.indexOf("?alt");
                fileName = url.substring(s, f);

                if (getMimeType(context, uri).equals("pdf"))
                    holder.listItems.add(new NumbersView(R.drawable.ic_pdf1, uri, Integer.toString(fileNo), fileName));
                else if (getMimeType(context, uri).equals("jpg"))
                    holder.listItems.add(new NumbersView(R.drawable.ic_image, uri, Integer.toString(fileNo), fileName));
                else
                    holder.listItems.add(new NumbersView(R.drawable.ic_file, uri, Integer.toString(fileNo), fileName));
                holder.adapter.notifyDataSetChanged();
                fileNo++;
            }
        }


    }

    @Override
    public int getItemCount() {

        return list.size();
    }
    public static String getMimeType(Context context, Uri uri) {
        String extension;

        //Check uri format to avoid null
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            //If scheme is a content
            final MimeTypeMap mime = MimeTypeMap.getSingleton();
            extension = mime.getExtensionFromMimeType(context.getContentResolver().getType(uri));
        } else {
            //If scheme is a File
            //This will replace white spaces with %20 and also other special characters. This will avoid returning null values on file name with spaces and special characters.
            extension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(new File(uri.getPath())).toString());

        }

        return extension;
    }
    public class feedViewAdapter extends RecyclerView.ViewHolder {
        private TextView authName,date,content;

        ListView listView;
        ArrayList<NumbersView> listItems;
        NumbersViewAdapter adapter;
        public feedViewAdapter(@NonNull @NotNull View itemView) {

            super(itemView);

            listView=itemView.findViewById(R.id.docList);
            authName=itemView.findViewById(R.id.authorName);
            date=itemView.findViewById(R.id.dateTime);
            content=itemView.findViewById(R.id.content);


        }
    }
}
