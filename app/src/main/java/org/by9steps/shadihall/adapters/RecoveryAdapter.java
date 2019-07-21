package org.by9steps.shadihall.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfDate;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

import org.by9steps.shadihall.AppController;
import org.by9steps.shadihall.R;
import org.by9steps.shadihall.activities.AddExpenseActivity;
import org.by9steps.shadihall.activities.BookingActivity;
import org.by9steps.shadihall.activities.CashCollectionActivity;
import org.by9steps.shadihall.activities.EventCashBookActivity;
import org.by9steps.shadihall.fragments.BookingDetailFragment;
import org.by9steps.shadihall.helper.DatabaseHelper;
import org.by9steps.shadihall.helper.Prefrence;
import org.by9steps.shadihall.model.Bookings;
import org.by9steps.shadihall.model.CashBook;
import org.by9steps.shadihall.model.Recovery;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class RecoveryAdapter extends RecyclerView.Adapter {

    private Context mCtx;
    List<Recovery> mList;

    Prefrence prefrence;
    DatabaseHelper databaseHelper;

    public RecoveryAdapter(Context mCtx, List<Recovery> mList) {
        this.mCtx = mCtx;
        this.mList = mList;
        prefrence = new Prefrence(mCtx);
        databaseHelper = new DatabaseHelper(mCtx);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0){
            View v = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
            v.findViewById(android.R.id.text1).setBackgroundColor(Color.parseColor("#f0749f"));
            return new MonthViewHolder(v);
        }else if (viewType == 2){
            LayoutInflater inflater = LayoutInflater.from(mCtx);
            View view = inflater.inflate(R.layout.recovery_group_total, null);
            return new RecoveryAdapter.TotalViewHolder(view);
        }else {
            LayoutInflater inflater = LayoutInflater.from(mCtx);
            View view = inflater.inflate(R.layout.recovery_list_item, null);
            return new RecoveryAdapter.ItemViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {

        final Recovery recovery = mList.get(position);

        if (recovery.isRow() == 1) {
            ((ItemViewHolder) viewHolder).bookingID.setText(recovery.getBookingID());
            ((ItemViewHolder) viewHolder).recieved.setText(recovery.getRecieved());
            ((ItemViewHolder) viewHolder).expensed.setText(recovery.getExpensed());
            ((ItemViewHolder) viewHolder).total_charges.setText(recovery.getChargesTotal());
            ((ItemViewHolder) viewHolder).balance.setText(recovery.getBalance());
            ((ItemViewHolder) viewHolder).profit.setText(recovery.getProfit());
            ((ItemViewHolder) viewHolder).event_name.setText(recovery.getEventName());

            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date date = sf.parse(recovery.getEventDate());
                SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
                String d = s.format(date);
                ((ItemViewHolder) viewHolder).event_date.setText(d);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            ((ItemViewHolder) viewHolder).client_name.setText(recovery.getClientName());

            if (AppController.addCB.equals("Hide")) {
                ((ItemViewHolder) viewHolder).add.setVisibility(View.GONE);
            } else {
                ((ItemViewHolder) viewHolder).add.setVisibility(View.VISIBLE);
            }

            ((ItemViewHolder) viewHolder).edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mCtx, BookingActivity.class);
                    intent.putExtra("BookingID",recovery.getBookingID());
                    intent.putExtra("Type","Edit");
                    mCtx.startActivity(intent);
                }
            });
            ((ItemViewHolder)viewHolder).print.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        createPdf(recovery.getBookingID());
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }
                }
            });

            ((ItemViewHolder) viewHolder).add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mCtx, EventCashBookActivity.class);
                    String incomeID = databaseHelper.getID("SELECT AcNameID FROM Account3Name WHERE ClientID = "+prefrence.getClientIDSession()+" and AcName = 'Booking Income'");
                    intent.putExtra("CreditAc", incomeID);
                    intent.putExtra("BookingId", recovery.getBookingID());
                    intent.putExtra("Type", "Income");
                    mCtx.startActivity(intent);

                }
            });

            ((ItemViewHolder) viewHolder).add_expense.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mCtx, EventCashBookActivity.class);
                    String expenseID = databaseHelper.getID("SELECT AcNameID FROM Account3Name WHERE ClientID = "+prefrence.getClientIDSession()+" and AcName = 'Booking Expense'");
                    intent.putExtra("CreditAc", expenseID);
                    intent.putExtra("BookingId", recovery.getBookingID());
                    intent.putExtra("Type", "Expense");
                    mCtx.startActivity(intent);
                }
            });


        }else if (recovery.isRow() == 2){
            ((TotalViewHolder) viewHolder).recieved.setText(recovery.getRecieved());
            ((TotalViewHolder) viewHolder).expensed.setText(recovery.getExpensed());
            ((TotalViewHolder) viewHolder).total_charges.setText(recovery.getChargesTotal());
            ((TotalViewHolder) viewHolder).balance.setText(recovery.getBalance());
            ((TotalViewHolder) viewHolder).profit.setText(recovery.getProfit());
        }else {
            MonthViewHolder h = (MonthViewHolder) viewHolder;
            h.textView.setText(recovery.getMonth());
        }

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        super.getItemViewType(position);
        Recovery item = mList.get(position);
        if(item.isRow() == 0) {
            return 0;
        }else if (item.isRow() == 2){
            return 2;
        }else {
            return 1;
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView recieved, expensed, total_charges, balance, profit, event_name, event_date, client_name, month, bookingID;
        ImageButton add, add_expense;
        Button edit, print;
        LinearLayout totalLayout;


        public ItemViewHolder(View itemView) {
            super(itemView);

            bookingID = itemView.findViewById(R.id.booking_id);
            recieved = itemView.findViewById(R.id.recieved);
            expensed = itemView.findViewById(R.id.expensed);
            total_charges = itemView.findViewById(R.id.total_charges);
            balance = itemView.findViewById(R.id.balance);
            profit = itemView.findViewById(R.id.profit);
            event_name = itemView.findViewById(R.id.event_name);
            event_date = itemView.findViewById(R.id.event_date);
            client_name = itemView.findViewById(R.id.client_name);
            add = itemView.findViewById(R.id.add);
            add_expense = itemView.findViewById(R.id.add_expense);
            totalLayout = itemView.findViewById(R.id.total_layout);
            month = itemView.findViewById(R.id.month);
            edit = itemView.findViewById(R.id.btn_edit);
            print = itemView.findViewById(R.id.btn_print);

        }
    }

    class TotalViewHolder extends RecyclerView.ViewHolder {

        TextView recieved, expensed, total_charges, balance, profit;
        ImageButton add, add_expense;
        LinearLayout totalLayout;


        public TotalViewHolder(View itemView) {
            super(itemView);

            recieved = itemView.findViewById(R.id.total_recieved);
            expensed = itemView.findViewById(R.id.total_expensed);
            total_charges = itemView.findViewById(R.id.g_total_charges);
            balance = itemView.findViewById(R.id.total_balance);
            profit = itemView.findViewById(R.id.total_profit);

        }
    }

    class MonthViewHolder extends RecyclerView.ViewHolder{

        private TextView textView;

        public MonthViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = (TextView) itemView.findViewById(android.R.id.text1);
        }
    }

    public void filterList(List<Recovery> filterdNames) {
        this.mList = filterdNames;
        notifyDataSetChanged();
    }

    //Create Booking Voucher
    //Print
    private static final String TAG = "PdfCreatorActivity";
    private File pdfFile;
    List<Bookings> bookingList;
    List<CashBook> cashBookList;

    public void createPdf(String id) throws IOException, DocumentException {

        databaseHelper = new DatabaseHelper(mCtx);
        String query = "SELECT * FROM Booking WHERE BookingID = '" + id+"'";
        bookingList = databaseHelper.getBookings(query);

        File docsFolder = new File(Environment.getExternalStorageDirectory() + "/Documents");
        if (!docsFolder.exists()) {
            docsFolder.mkdir();
            Log.i(TAG, "Created a new directory for PDF");
        }

        String pdfname = "Booking.pdf";
        pdfFile = new File(docsFolder.getAbsolutePath(), pdfname);
        if (pdfFile.exists()){
            pdfFile.delete();
        }
        for (Bookings b : bookingList) {
            OutputStream output = new FileOutputStream(pdfFile);

            Document document = new Document(PageSize.A4);
            PdfWriter writer = PdfWriter.getInstance(document, output);
            writer.createXmpMetadata();
            writer.setTagged();
            writer.setPageEvent(new Footer());
            document.open();
            document.addLanguage("en-us");

            PdfDictionary parameters = new PdfDictionary();
            parameters.put(PdfName.MODDATE, new PdfDate());

            Font chapterFont = FontFactory.getFont(FontFactory.HELVETICA, 13, Font.BOLD);
            Font paragraphFont = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD);

            //Header
            PdfPTable header = new PdfPTable(new float[]{2, 8});
            header.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            header.getDefaultCell().setMinimumHeight(300);
            header.setTotalWidth(PageSize.A4.getWidth());
            header.setWidthPercentage(100);
            header.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
            header.setSpacingBefore(5);

            PdfPTable nestedTable = new PdfPTable(new float[]{7});
            nestedTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            nestedTable.setTotalWidth(PageSize.A4.getWidth());
            nestedTable.setWidthPercentage(100);
            nestedTable.getDefaultCell().setVerticalAlignment(Element.ALIGN_TOP);
            nestedTable.setSpacingAfter(3);
            Chunk chunk = new Chunk("Abc ShadiHall", chapterFont);
            PdfPCell c = new PdfPCell();
            c.setBorder(Rectangle.NO_BORDER);
            c.addElement(chunk);
            nestedTable.addCell(c);
            nestedTable.addCell(footerCell("Address", PdfPCell.ALIGN_LEFT));
            nestedTable.addCell(footerCell("Number", PdfPCell.ALIGN_LEFT));
            nestedTable.addCell(footerCell("Email", PdfPCell.ALIGN_LEFT));
            nestedTable.addCell(footerCell("Website", PdfPCell.ALIGN_LEFT));

            header.addCell(getCell("", PdfPCell.ALIGN_CENTER));
            PdfPCell hCell = new PdfPCell();
            hCell.addElement(nestedTable);
            header.addCell(hCell);

            //Booking Voucher
            PdfPTable title = new PdfPTable(new float[]{3, 3, 3});
            title.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            title.getDefaultCell().setMinimumHeight(60);
            title.setTotalWidth(PageSize.A4.getWidth());
            title.setWidthPercentage(100);
            title.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
            title.setSpacingBefore(10);
            title.addCell(footerCell("", PdfPCell.ALIGN_CENTER));
            PdfPCell cell = new PdfPCell(new Phrase("Booking Voucher", chapterFont));
            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_TOP);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cell.setPaddingBottom(8);
            title.addCell(cell);
            title.addCell(footerCell("", PdfPCell.ALIGN_CENTER));

            //Booking Date
            PdfPTable header1 = new PdfPTable(new float[]{1});
            header1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            header1.getDefaultCell().setMinimumHeight(300);
            header1.setTotalWidth(PageSize.A4.getWidth());
            header1.setWidthPercentage(100);
            header1.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
            header1.setSpacingBefore(10);

            PdfPTable nestedTable1 = new PdfPTable(new float[]{1});
            nestedTable1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            nestedTable1.setTotalWidth(PageSize.A4.getWidth());
            nestedTable1.setWidthPercentage(100);
            nestedTable1.getDefaultCell().setVerticalAlignment(Element.ALIGN_TOP);
            nestedTable1.setSpacingBefore(3);
            nestedTable1.setSpacingAfter(3);
            Date dat = new Date();
            SimpleDateFormat df = new SimpleDateFormat("EEEE, d MMM yyyy");
            PdfPCell ce = new PdfPCell(new Phrase(AppController.stringDateFormate("yyyy-MM-dd", "EEEE, d MMM yyyy", b.getEventDate()), chapterFont));
            ce.setBorder(Rectangle.NO_BORDER);
//        ce.addElement(ch);
            ce.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            ce.setVerticalAlignment(PdfPCell.ALIGN_CENTER);
            nestedTable1.addCell(ce);
            nestedTable1.addCell(footerCell(b.getShift()+" Shift", PdfPCell.ALIGN_CENTER));
            nestedTable1.addCell(footerCell(b.getArrangePersons()+" Persons("+b.getEventName()+")", PdfPCell.ALIGN_CENTER));

            PdfPCell hCell1 = new PdfPCell();
            hCell1.addElement(nestedTable1);
            header1.addCell(hCell1);

            //Customer Detail
            PdfPTable header2 = new PdfPTable(new float[]{6, 4});
            header2.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            header2.getDefaultCell().setMinimumHeight(300);
            header2.setTotalWidth(PageSize.A4.getWidth());
            header2.setWidthPercentage(100);
            header2.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
            header2.setSpacingBefore(5);

            PdfPTable nestedTable2 = new PdfPTable(new float[]{4});
            nestedTable2.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            nestedTable2.setTotalWidth(PageSize.A4.getWidth());
            nestedTable2.setWidthPercentage(100);
            nestedTable2.getDefaultCell().setVerticalAlignment(Element.ALIGN_TOP);
            nestedTable2.setSpacingBefore(3);
            nestedTable2.setSpacingAfter(3);
            String[] separated = b.getBookingDate().split(" ");
            nestedTable2.addCell(footerCell("Booked on Date: "+separated[0], PdfPCell.ALIGN_LEFT));
            nestedTable2.addCell(footerCell("Booking ID : "+b.getBookingID(), PdfPCell.ALIGN_LEFT));
            nestedTable2.addCell(footerCell("_____________________________", PdfPCell.ALIGN_TOP));
            nestedTable2.addCell(footerCell("Total Booking Charges", PdfPCell.ALIGN_CENTER));
            PdfPCell p = new PdfPCell(new Phrase(b.getChargesTotal(), chapterFont));
            p.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            p.setBorder(Rectangle.NO_BORDER);
            nestedTable2.addCell(p);

            PdfPTable nestedTable3 = new PdfPTable(new float[]{3.2f, 6.8f});
            nestedTable3.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            nestedTable3.setTotalWidth(PageSize.A4.getWidth());
            nestedTable3.setWidthPercentage(100);
            nestedTable3.getDefaultCell().setVerticalAlignment(Element.ALIGN_TOP);
            nestedTable3.setSpacingBefore(3);
            nestedTable3.setSpacingAfter(3);
            nestedTable3.addCell(footerCell("Customer Name: ", PdfPCell.ALIGN_LEFT));
            nestedTable3.addCell(footerCell(b.getClientName(), PdfPCell.ALIGN_LEFT));

            nestedTable3.addCell(footerCell("Mobile No: ", PdfPCell.ALIGN_LEFT));
            nestedTable3.addCell(footerCell(b.getClientMobile(), PdfPCell.ALIGN_LEFT));

            nestedTable3.addCell(footerCell("ID Card No: ", PdfPCell.ALIGN_LEFT));
            nestedTable3.addCell(footerCell(b.getClientNic(), PdfPCell.ALIGN_LEFT));

            nestedTable3.addCell(footerCell("Home Address: ", PdfPCell.ALIGN_LEFT));
            nestedTable3.addCell(footerCell(b.getClientAddress(), PdfPCell.ALIGN_LEFT));

            nestedTable3.addCell(footerCell("Description: ", PdfPCell.ALIGN_LEFT));
            nestedTable3.addCell(footerCell(b.getDescription(), PdfPCell.ALIGN_LEFT));

            PdfPCell hCell3 = new PdfPCell();
            hCell3.addElement(nestedTable3);
            header2.addCell(hCell3);
            PdfPCell hCell2 = new PdfPCell();
            hCell2.addElement(nestedTable2);
            header2.addCell(hCell2);

            PdfPTable table = new PdfPTable(new float[]{4});
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            table.setTotalWidth(PageSize.A4.getWidth());
            table.getDefaultCell().setFixedHeight(25);
            table.setWidthPercentage(100);
            table.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
            table.setSpacingBefore(3);
            table.addCell(getCell(BookingDetailFragment.EnglishNumberToWords.convert(Integer.valueOf(b.getChargesTotal())), PdfPCell.ALIGN_RIGHT));
            table.addCell(footerCell("Receiving Detail", PdfPCell.ALIGN_LEFT));

            //Receiving Detail Table
            PdfPTable table1 = new PdfPTable(new float[]{2, 2, 6, 2});
            table1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.getDefaultCell().setFixedHeight(25);
            table1.setTotalWidth(PageSize.A4.getWidth());
            table1.setWidthPercentage(100);
            table1.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
            table1.setSpacingBefore(4);

            Font whiteColor = FontFactory.getFont(FontFactory.HELVETICA, 13, Font.BOLD);
            whiteColor.setColor(BaseColor.WHITE);
            PdfPCell c1 = new PdfPCell(new Phrase("S/No",whiteColor));
            c1.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            table1.addCell(c1);
            PdfPCell c2 = new PdfPCell(new Phrase("Date",whiteColor));
            c2.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            table1.addCell(c2);
            PdfPCell c3 = new PdfPCell(new Phrase("Remarks",whiteColor));
            c3.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
            table1.addCell(c3);
            PdfPCell c4 = new PdfPCell(new Phrase("Amount",whiteColor));
            c4.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            table1.addCell(c4);

            table1.setHeaderRows(1);
            PdfPCell[] cells = table1.getRow(0).getCells();
            for (int j = 0; j < cells.length; j++) {
                cells[j].setBackgroundColor(BaseColor.BLACK);
            }

            String qu = "SELECT * FROM CashBook WHERE BookingID = "+id;
            cashBookList = databaseHelper.getCashBook(qu);
            int no = 1, gTotal = 0;
            for (CashBook cb : cashBookList){
                table1.addCell(getCell(String.valueOf(no), PdfPCell.ALIGN_CENTER));
                table1.addCell(getCell(cb.getCBDate(), PdfPCell.ALIGN_CENTER));
                table1.addCell(getCell(cb.getCBRemarks(), PdfPCell.ALIGN_LEFT));
                table1.addCell(getCell(cb.getAmount(), PdfPCell.ALIGN_RIGHT));
                no = no + 1;
                gTotal = gTotal + Integer.valueOf(cb.getAmount());
            }

            for (int i = 1; i <= cashBookList.size(); i++) {
                PdfPCell[] cellss = table1.getRow(i).getCells();
                for (int j = 0; j < cellss.length; j++) {

                    if (i != i%2) {
                        cellss[j].setBackgroundColor(BaseColor.PINK);
                        Log.e("COLOR", String.valueOf(BaseColor.PINK));
                    } else {
                        cellss[j].setBackgroundColor(BaseColor.LIGHT_GRAY);
                    }
                }
            }
            table1.addCell(footerCell("", PdfPCell.ALIGN_CENTER));
            table1.addCell(footerCell("", PdfPCell.ALIGN_CENTER));
            table1.addCell(getCell("Total", PdfPCell.ALIGN_LEFT));
            table1.addCell(getCell(String.valueOf(gTotal), PdfPCell.ALIGN_RIGHT));

            //Terms & Conditions
            PdfPTable terms = new PdfPTable(new float[]{1});
            terms.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            terms.getDefaultCell().setMinimumHeight(300);
            terms.setTotalWidth(PageSize.A4.getWidth());
            terms.setWidthPercentage(100);
            terms.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
            terms.setSpacingBefore(5);

            PdfPTable nestedTable4 = new PdfPTable(new float[]{1});
            nestedTable4.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            nestedTable4.setTotalWidth(PageSize.A4.getWidth());
            nestedTable4.setWidthPercentage(100);
            nestedTable4.getDefaultCell().setVerticalAlignment(Element.ALIGN_TOP);
            nestedTable4.setSpacingBefore(3);
            nestedTable4.setSpacingAfter(3);
            nestedTable4.addCell(footerCell("Terms & Conditions", PdfPCell.ALIGN_CENTER));
            nestedTable4.addCell(footerCell("1.-----------------------", PdfPCell.ALIGN_LEFT));
            nestedTable4.addCell(footerCell("2.-----------------------", PdfPCell.ALIGN_LEFT));
            nestedTable4.addCell(footerCell("3.-----------------------", PdfPCell.ALIGN_LEFT));
            nestedTable4.addCell(footerCell("4.-----------------------", PdfPCell.ALIGN_LEFT));

            PdfPCell hCell4 = new PdfPCell();
            hCell4.addElement(nestedTable4);
            terms.addCell(hCell4);

            //Signatures
            PdfPTable sign = new PdfPTable(new float[]{3, 3, 3});
            sign.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            sign.getDefaultCell().setMinimumHeight(300);
            sign.setTotalWidth(PageSize.A4.getWidth());
            sign.setWidthPercentage(100);
            sign.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
            sign.setSpacingBefore(10);
            sign.addCell(getCell("Prepared By _____________", PdfPCell.ALIGN_CENTER));
            sign.addCell(getCell("Approved By _____________", PdfPCell.ALIGN_CENTER));
            sign.addCell(getCell("Customer By _____________", PdfPCell.ALIGN_CENTER));


            document.open();

            document.add(header);
            document.add(title);
            document.add(header1);
            document.add(header2);
            document.add(table);
            document.add(table1);
            document.add(terms);
            document.add(sign);

            document.close();
            customPDFView();
            Log.e("PDFDocument", "Created");
        }
    }

    public PdfPCell getCell(String text, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(alignment);
        cell.setMinimumHeight(25);
        cell.setPadding(2);
        return cell;
    }

    public PdfPCell footerCell(String text, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text));
        cell.setPadding(3);
        cell.setHorizontalAlignment(alignment);
        cell.setMinimumHeight(15);
        cell.setBorder(PdfPCell.NO_BORDER);
        return cell;
    }

    public void customPDFView(){
        PackageManager packageManager = mCtx.getPackageManager();
        Intent testIntent = new Intent(Intent.ACTION_VIEW);
        testIntent.setType("application/pdf");
        List list = packageManager.queryIntentActivities(testIntent, PackageManager.MATCH_DEFAULT_ONLY);
        if (list.size() > 0) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            Uri uri = Uri.fromFile(pdfFile);
            intent.setDataAndType(uri, "application/pdf");
            mCtx.startActivity(intent);
        } else {
            Toast.makeText(mCtx, "Download a PDF Viewer to see the generated PDF", Toast.LENGTH_SHORT).show();
        }
    }

    class Footer extends PdfPageEventHelper {
        Font font;
        PdfTemplate t;
        Image total;

        @Override
        public void onOpenDocument(PdfWriter writer, Document document) {
            t = writer.getDirectContent().createTemplate(30, 16);
            try {
                total = Image.getInstance(t);
                total.setRole(PdfName.ARTIFACT);
                font =  new Font(Font.FontFamily.TIMES_ROMAN, 30.0f, Font.UNDERLINE, BaseColor.BLACK);
            } catch (DocumentException de) {
                throw new ExceptionConverter(de);
            }
        }

        @Override
        public void onEndPage(PdfWriter writer, Document document) {

            PdfPTable table = new PdfPTable(new float[]{3, 4, 2});
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            table.getDefaultCell().setFixedHeight(20);
            table.setTotalWidth(PageSize.A4.getWidth());
            table.getDefaultCell().setBorder(Rectangle.BOTTOM);
            Date dat = new Date();
            SimpleDateFormat df = new SimpleDateFormat("EEE, d MMM yyyy");
            table.addCell(footerCell(df.format(dat), PdfPCell.ALIGN_LEFT));
            table.addCell(footerCell("www.easysoft.com.pk",PdfPCell.ALIGN_LEFT));
            table.addCell(footerCell(String.format("Page %d ", writer.getPageNumber() -1),PdfPCell.ALIGN_LEFT));

            PdfContentByte canvas = writer.getDirectContent();
            canvas.beginMarkedContentSequence(PdfName.ARTIFACT);
            table.writeSelectedRows(0, -1, 36, 30, canvas);
            canvas.endMarkedContentSequence();

        }

        @Override
        public void onCloseDocument(PdfWriter writer, Document document) {
            ColumnText.showTextAligned(t, Element.ALIGN_LEFT,
                    new Phrase(String.valueOf(writer.getPageNumber()), font),
                    2, 4, 0);
        }
    }

    public static class EnglishNumberToWords {

        private static final String[] tensNames = { "", " ten", " twenty", " thirty", " forty",
                " fifty", " sixty", " seventy", " eighty", " ninety" };

        private static final String[] numNames = { "", " one", " two", " three", " four", " five",
                " six", " seven", " eight", " nine", " ten", " eleven", " twelve", " thirteen",
                " fourteen", " fifteen", " sixteen", " seventeen", " eighteen", " nineteen" };

        private static String convertLessThanOneThousand(int number)
        {
            String soFar;

            if (number % 100 < 20)
            {
                soFar = numNames[number % 100];
                number /= 100;
            } else
            {
                soFar = numNames[number % 10];
                number /= 10;

                soFar = tensNames[number % 10] + soFar;
                number /= 10;
            }
            if (number == 0)
                return soFar;
            return numNames[number] + " hundred" + soFar;
        }

        public static String convert(long number)
        {
            // 0 to 999 999 999 999
            if (number == 0)
            {
                return "zero";
            }

            String snumber = Long.toString(number);

            // pad with "0"
            String mask = "000000000000";
            DecimalFormat df = new DecimalFormat(mask);
            snumber = df.format(number);

            // XXXnnnnnnnnn
            int billions = Integer.parseInt(snumber.substring(0, 3));
            // nnnXXXnnnnnn
            int millions = Integer.parseInt(snumber.substring(3, 6));
            // nnnnnnXXXnnn
            int hundredThousands = Integer.parseInt(snumber.substring(6, 9));
            // nnnnnnnnnXXX
            int thousands = Integer.parseInt(snumber.substring(9, 12));

            String tradBillions;
            switch (billions)
            {
                case 0:
                    tradBillions = "";
                    break;
                case 1:
                    tradBillions = convertLessThanOneThousand(billions) + " billion ";
                    break;
                default:
                    tradBillions = convertLessThanOneThousand(billions) + " billion ";
            }
            String result = tradBillions;

            String tradMillions;
            switch (millions)
            {
                case 0:
                    tradMillions = "";
                    break;
                case 1:
                    tradMillions = convertLessThanOneThousand(millions) + " million ";
                    break;
                default:
                    tradMillions = convertLessThanOneThousand(millions) + " million ";
            }
            result = result + tradMillions;

            String tradHundredThousands;
            switch (hundredThousands)
            {
                case 0:
                    tradHundredThousands = "";
                    break;
                case 1:
                    tradHundredThousands = "one thousand ";
                    break;
                default:
                    tradHundredThousands = convertLessThanOneThousand(hundredThousands) + " thousand ";
            }
            result = result + tradHundredThousands;

            String tradThousand;
            tradThousand = convertLessThanOneThousand(thousands);
            result = result + tradThousand;

            // remove extra spaces!
            return result.replaceAll("^\\s+", "").replaceAll("\\b\\s{2,}\\b", " ")+ " only";
        }

    }
}
