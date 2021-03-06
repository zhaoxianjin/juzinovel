package top.iscore.freereader.adapter.holders;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import top.iscore.freereader.R;
import top.iscore.freereader.fragment.adapters.CommonViewHolder;
import xcvf.top.readercore.bean.Book;

/**
 * 书籍
 * Created by xiaw on 2018/9/18.
 */
public class BookShelfHolder extends CommonViewHolder<Book> {


    @BindView(R.id.img_cover)
    ImageView imgCover;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_chapter)
    TextView tvChapter;
    @BindView(R.id.ll_new)
    LinearLayout llNew;
    @BindView(R.id.ll_empty)
    LinearLayout llEmpty;
    @BindView(R.id.ll_content)
    LinearLayout llContent;
    @BindView(R.id.tv_engine)
    TextView tvEngine;
    @BindView(R.id.tv_author)
    TextView tvAuthor;
    @BindView(R.id.book_content)
    LinearLayout bookContent;

    public BookShelfHolder(Context context, ViewGroup root) {
        super(context, root, R.layout.item_holder_bookself);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bindData(Book book, int position) {

        if (book.bookid == -1) {
            llEmpty.setVisibility(View.VISIBLE);
            llContent.setVisibility(View.GONE);
        } else {
            llContent.setVisibility(View.VISIBLE);
            llEmpty.setVisibility(View.GONE);
            RoundedCornersTransformation roundedCornersTransformation
                    = new RoundedCornersTransformation(itemView.getContext(), 10, 0);
            Glide.with(itemView.getContext()).load(book.cover).placeholder(R.color.text_gray_light).bitmapTransform(roundedCornersTransformation).into(imgCover);
            tvName.setText(Html.fromHtml(book.name + ""));
            tvAuthor.setText(" | " + book.author);
            if (TextUtils.isEmpty(book.chapter_name)) {
                tvChapter.setText("暂无阅读历史");
            } else {
                tvChapter.setText("读到： " + book.chapter_name);
            }

            llNew.setVisibility(View.INVISIBLE);
        }

        //tvEngine.setText(book.engine_domain.substring(12,13));

    }
}
