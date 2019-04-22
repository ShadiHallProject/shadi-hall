package org.by9steps.shadihall.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import org.by9steps.shadihall.AppController;
import org.by9steps.shadihall.R;
import org.by9steps.shadihall.activities.CashCollectionActivity;
import org.by9steps.shadihall.helper.DatabaseHelper;
import org.by9steps.shadihall.model.CBSetting;
import org.by9steps.shadihall.model.CashBook;
import org.by9steps.shadihall.model.CashEntry;
import org.by9steps.shadihall.model.User;
import org.by9steps.shadihall.model.Voucher;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;



public class CashBookAdapter extends RecyclerView.Adapter {

    private Context mCtx;
    List<CashEntry> mList;
    List<CashEntry> mFilterList;
    List<CashBook> printList;

    File pdfFile;
    DatabaseHelper databaseHelper;

    DecimalFormat formatter = new DecimalFormat("#,###,###");

    public CashBookAdapter(Context mCtx, List<CashEntry> mList) {
        this.mCtx = mCtx;
        this.mList = mList;
        this.mFilterList = mList;
        databaseHelper = new DatabaseHelper(mCtx);
        Log.e("LIST","sss");
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0){
            View v = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
            v.findViewById(android.R.id.text1).setBackgroundColor(Color.parseColor("#f0749f"));
            return new CashBookAdapter.MonthViewHolder(v);
        }else if (viewType == 2){
            LayoutInflater inflater = LayoutInflater.from(mCtx);
            View view = inflater.inflate(R.layout.cash_book_group_total, null);
            return new CashBookAdapter.TotalViewHolder(view);
        }else {
            LayoutInflater inflater = LayoutInflater.from(mCtx);
            View view = inflater.inflate(R.layout.cash_book_entry_item, null);
            return new CashBookAdapter.ItemViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {

        final CashEntry cashEntry = mList.get(position);

        if (cashEntry.isRow() == 1) {

            List<CBSetting> list = CBSetting.listAll(CBSetting.class);
            for (CBSetting c : list){
                if (!c.getDste()){
                    ((ItemViewHolder) viewHolder).date.setVisibility(View.GONE);
                }else {
                    ((ItemViewHolder) viewHolder).date.setVisibility(View.VISIBLE);
                }

                if (!c.getCbId()){
                    ((ItemViewHolder) viewHolder).cashBookID.setVisibility(View.GONE);
                }else {
                    ((ItemViewHolder) viewHolder).cashBookID.setVisibility(View.VISIBLE);
                }

                if (!c.getDebit()){
                    ((ItemViewHolder) viewHolder).deb_account.setVisibility(View.GONE);
                }else {
                    ((ItemViewHolder) viewHolder).deb_account.setVisibility(View.VISIBLE);
                }

                if (!c.getCredit()){
                    ((ItemViewHolder) viewHolder).cre_account.setVisibility(View.GONE);
                }else {
                    ((ItemViewHolder) viewHolder).cre_account.setVisibility(View.VISIBLE);
                }

                if (!c.getRemarks()){
                    ((ItemViewHolder) viewHolder).remarks.setVisibility(View.GONE);
                }else {
                    ((ItemViewHolder) viewHolder).remarks.setVisibility(View.VISIBLE);
                }

                if (!c.getAmount()){
                    ((ItemViewHolder) viewHolder).amount.setVisibility(View.GONE);
                }else {
                    ((ItemViewHolder) viewHolder).amount.setVisibility(View.VISIBLE);
                }
            }

            ((ItemViewHolder) viewHolder).cashBookID.setText(cashEntry.getCashBookID());
            ((ItemViewHolder) viewHolder).deb_account.setText(cashEntry.getDebitAccountName());
            ((ItemViewHolder) viewHolder).cre_account.setText(cashEntry.getCreditAccountName());
            ((ItemViewHolder) viewHolder).amount.setText(cashEntry.getAmount());
            ((ItemViewHolder) viewHolder).remarks.setText(cashEntry.getCBRemarks());
            ((ItemViewHolder) viewHolder).date.setText(cashEntry.getCBDate());

            ((ItemViewHolder) viewHolder).edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mCtx, CashCollectionActivity.class);
                    intent.putExtra("BookingID","0");
                    intent.putExtra("Spinner","View");
                    intent.putExtra("Type","Edit");
                    intent.putExtra("CashBookID",cashEntry.getCashBookID());
                    mCtx.startActivity(intent);
                }
            });
            ((ItemViewHolder)viewHolder).print.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    createPDF(cashEntry.getCashBookID());
                }
            });


        }else if (cashEntry.isRow() == 2) {
            ((TotalViewHolder) viewHolder).amount.setText(cashEntry.getAmount());
        }else {
            CashBookAdapter.MonthViewHolder h = (CashBookAdapter.MonthViewHolder) viewHolder;
            h.textView.setText(cashEntry.getMonth());
        }

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        super.getItemViewType(position);
        CashEntry item = mList.get(position);
        if(item.isRow() == 0) {
            return 0;
        }else if (item.isRow() == 2){
            return 2;
        }else {
            return 1;
        }
    }

    public void filterList(List<CashEntry> filterdNames) {
        this.mList = filterdNames;
        notifyDataSetChanged();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView cashBookID, deb_account, cre_account, amount, remarks, date;
        Button edit,print;


        public ItemViewHolder(View itemView) {
            super(itemView);

            cashBookID = itemView.findViewById(R.id.cashBookID);
            cre_account = itemView.findViewById(R.id.cre_account);
            deb_account = itemView.findViewById(R.id.deb_account);
            amount = itemView.findViewById(R.id.amount);
            remarks = itemView.findViewById(R.id.remarks);
            date = itemView.findViewById(R.id.date);
            edit = itemView.findViewById(R.id.btn_edit);
            print = itemView.findViewById(R.id.btn_print);

        }
    }

    class TotalViewHolder extends RecyclerView.ViewHolder {

        TextView amount, total;


        public TotalViewHolder(View itemView) {
            super(itemView);

            amount = itemView.findViewById(R.id.amount);
            total = itemView.findViewById(R.id.total);

        }
    }

    class MonthViewHolder extends RecyclerView.ViewHolder{

        private TextView textView;

        public MonthViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = (TextView) itemView.findViewById(android.R.id.text1);
        }
    }

    public void customPDFView(){

        File docsFolder = new File(Environment.getExternalStorageDirectory() + "/Documents");
        String pdfname = "Voucher.pdf";
        pdfFile = new File(docsFolder.getAbsolutePath(), pdfname);
        Log.e("PDFFOLDER",pdfFile.toString());

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

    public void createPDF (String id){
        List<Voucher> mVoucher = null;

        List<User> list = User.listAll(User.class);
        for (User u : list){
            String query = "SELECT        CashBook.CashBookID, CashBook.CBDate, CashBook.CBRemarks, CashBook.DebitAccount, CashBook.CreditAccount, CashBook.ClientUserID, CashBook.Amount, \n" +
                    "                         CashBook.ClientID, Account3Name_1.AcName AS DebiterName, Account3Name_1.AcAddress AS DebiterAddress, Account3Name_1.AcContactNo AS DebiterContactNo,\n" +
                    "                          Account3Name_2.AcName AS CrediterName, Account3Name_2.AcAddress AS CrediterAddress, Account3Name_2.AcContactNo AS CrediterContactNo, \n" +
                    "                         Account3Name.AcName AS PreparedBy\n" +
                    "FROM            CashBook LEFT OUTER JOIN\n" +
                    "                         Account3Name ON CashBook.ClientUserID = Account3Name.AcNameID LEFT OUTER JOIN\n" +
                    "                         Account3Name AS Account3Name_2 ON CashBook.CreditAccount = Account3Name_2.AcNameID LEFT OUTER JOIN\n" +
                    "                         Account3Name AS Account3Name_1 ON CashBook.DebitAccount = Account3Name_1.AcNameID\n" +
                    "WHERE        (CashBook.CashBookID = "+id+") AND (CashBook.ClientID = "+u.getClientID()+")";
            mVoucher = databaseHelper.getPrintValues(query);
        }

        for (Voucher v : mVoucher){
            try {
                Log.e("CREATE","CREATE1");
//            OutputStream file = new FileOutputStream(new File(pdfFilename));
//            Document document = new Document();
//            PdfWriter.getInstance(document, file);

                File docsFolder = new File(Environment.getExternalStorageDirectory() + "/Documents");
                if (!docsFolder.exists()) {
                    docsFolder.mkdir();
                }
                String pdfname = "Voucher.pdf";
                File pdfFile = new File(docsFolder.getAbsolutePath(), pdfname);
                OutputStream output = new FileOutputStream(pdfFile);
                Document document = new Document(PageSize.A4);
                Log.e("CREATE","CREATE2");

                //Inserting Image in PDF
                Drawable d = mCtx.getResources ().getDrawable (R.drawable.logo);
                Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] bitmapData = stream.toByteArray();
                Image image = Image.getInstance (bitmapData);//Header Image
                image.scaleAbsolute(540f, 72f);//image width,height


                Font chapterFont = FontFactory.getFont(FontFactory.HELVETICA, 16, Font.BOLD);
                Font paragraphFont = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD);
                Chunk chunk = new Chunk("Client Name", chapterFont);
                Paragraph name = new Paragraph("Address",paragraphFont);
                name.setIndentationLeft(0);
                Paragraph contact = new Paragraph("Contact",paragraphFont);
                contact.setIndentationLeft(0);

                PdfPTable table = new PdfPTable(3);
                table.setWidthPercentage(100);
                table.setSpacingBefore(20);
                table.addCell(getCell("Voucher ID: "+v.getCashBookID(), PdfPCell.ALIGN_LEFT));
                table.addCell(getCell(" ", PdfPCell.ALIGN_CENTER));
                table.addCell(getCell("Date: "+ AppController.stringDateFormate("yyyy-MM-dd","dd-MM-yyyy",v.getCBDate()), PdfPCell.ALIGN_RIGHT));


                PdfPTable billTable = new PdfPTable(1); //one page contains 15 records
                billTable.setWidthPercentage(100);
                billTable.setWidths(new float[] { 1 });
                billTable.setSpacingBefore(5);
                Font voucherFount = FontFactory.getFont(FontFactory.HELVETICA, 18, Font.BOLD);
                voucherFount.setColor(BaseColor.WHITE);
                PdfPCell voucher = new PdfPCell(new Phrase("Voucher",voucherFount));
                voucher.setHorizontalAlignment(Element.ALIGN_CENTER);
                voucher.setVerticalAlignment(Element.ALIGN_CENTER);
                voucher.setBackgroundColor(BaseColor.BLACK);
                billTable.addCell(voucher);
                PdfPCell remarks = new PdfPCell(new Phrase("Remarks: "+v.getCBRemarks()));
                billTable.addCell(remarks);

                PdfPTable table1 = new PdfPTable(3);
                table1.setWidthPercentage(100);
                table1.setWidths(new float[] { 3, 1, 1 });
                PdfPCell particular = new PdfPCell(new Phrase("Particular",voucherFount));
                PdfPCell debit = new PdfPCell(new Phrase("Debit",voucherFount));
                PdfPCell credit = new PdfPCell(new Phrase("Credit",voucherFount));
                particular.setBackgroundColor(BaseColor.BLACK);
                debit.setBackgroundColor(BaseColor.BLACK);
                credit.setBackgroundColor(BaseColor.BLACK);
                particular.setHorizontalAlignment(Element.ALIGN_CENTER);
                debit.setHorizontalAlignment(Element.ALIGN_CENTER);
                credit.setHorizontalAlignment(Element.ALIGN_CENTER);
                particular.setVerticalAlignment(Element.ALIGN_CENTER);
                debit.setVerticalAlignment(Element.ALIGN_CENTER);
                credit.setVerticalAlignment(Element.ALIGN_CENTER);
                particular.setBorderColor(BaseColor.WHITE);
                particular.setBorderWidth(2f);
                debit.setBorderColor(BaseColor.WHITE);
                debit.setBorderWidth(2f);
                credit.setBorderColor(BaseColor.WHITE);
                credit.setBorderWidth(2f);
                table1.addCell(particular);
                table1.addCell(debit);
                table1.addCell(credit);


                Font debiterFont = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD);
                PdfPCell debiter = new PdfPCell(new Phrase("Debiter",debiterFont));
                debiter.setVerticalAlignment(Element.ALIGN_CENTER);
                debiter.setBackgroundColor(BaseColor.LIGHT_GRAY);
                PdfPCell emp1 = new PdfPCell(new Phrase(""));
                PdfPCell emp2 = new PdfPCell(new Phrase(""));
                table1.addCell(debiter);
                table1.addCell(emp1);
                table1.addCell(emp2);

                Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD);
                PdfPCell debiterName = new PdfPCell(new Phrase(v.getDebiterName(),cellFont));
                PdfPCell debitAmount = new PdfPCell(new Phrase(formatter.format(Integer.valueOf(v.getAmount())),cellFont));
                debitAmount.setHorizontalAlignment(Element.ALIGN_RIGHT);
                debitAmount.setVerticalAlignment(Element.ALIGN_CENTER);
                debiterName.setVerticalAlignment(Element.ALIGN_CENTER);
                PdfPCell emp3 = new PdfPCell(new Phrase("  "));
                table1.addCell(debiterName);
                table1.addCell(debitAmount);
                table1.addCell(emp3);
                table1.addCell(emp3);
                table1.addCell(emp3);
                table1.addCell(emp3);
                table1.addCell(emp3);
                table1.addCell(emp3);
                table1.addCell(emp3);

                PdfPCell crediter = new PdfPCell(new Phrase("Crediter",debiterFont));
                crediter.setBackgroundColor(BaseColor.LIGHT_GRAY);
                crediter.setVerticalAlignment(Element.ALIGN_CENTER);
                table1.addCell(crediter);
                table1.addCell(emp3);
                table1.addCell(emp3);

                PdfPCell crediterName = new PdfPCell(new Phrase(v.getCrediterName(),cellFont));
                PdfPCell creditAmount = new PdfPCell(new Phrase(formatter.format(Integer.valueOf(v.getAmount())),cellFont));
                creditAmount.setHorizontalAlignment(Element.ALIGN_RIGHT);
                creditAmount.setVerticalAlignment(Element.ALIGN_CENTER);
                crediterName.setVerticalAlignment(Element.ALIGN_CENTER);
                table1.addCell(crediterName);
                table1.addCell(emp1);
                table1.addCell(creditAmount);

                Font nCellFont = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL);
                PdfPCell crediterAddress = new PdfPCell(new Phrase(v.getCrediterAddress(),nCellFont));
                PdfPCell crediterNumber = new PdfPCell(new Phrase(v.getCrediterContactNo(),nCellFont));
                crediterAddress.setVerticalAlignment(Element.ALIGN_CENTER);
                crediterNumber.setVerticalAlignment(Element.ALIGN_CENTER);
                table1.addCell(crediterAddress);
                table1.addCell(emp1);
                table1.addCell(emp2);
                table1.addCell(crediterNumber);
                table1.addCell(emp1);
                table1.addCell(emp2);

                Font totalFont = FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD);
                PdfPCell total = new PdfPCell(new Phrase("Total",totalFont));
                PdfPCell creditTotal = new PdfPCell(new Phrase(formatter.format(Integer.valueOf(v.getAmount())),totalFont));
                PdfPCell debitTotal = new PdfPCell(new Phrase(formatter.format(Integer.valueOf(v.getAmount())),totalFont));
                creditTotal.setHorizontalAlignment(Element.ALIGN_RIGHT);
                debitTotal.setHorizontalAlignment(Element.ALIGN_RIGHT);
                total.setVerticalAlignment(Element.ALIGN_CENTER);
                debitTotal.setVerticalAlignment(Element.ALIGN_CENTER);
                creditTotal.setVerticalAlignment(Element.ALIGN_CENTER);
                table1.addCell(total);
                table1.addCell(debitTotal);
                table1.addCell(creditTotal);

                PdfPTable table2 = new PdfPTable(1); //one page contains 15 records
                table2.setWidthPercentage(100);
                table2.setSpacingBefore(5);
                table2.setWidths(new float[] { 1 });
                Font table2Fount = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.NORMAL);

                PdfPCell amm = new PdfPCell(new Phrase(EnglishNumberToWords.convert(Integer.valueOf(v.getAmount())),table2Fount));
                amm.setHorizontalAlignment(Element.ALIGN_RIGHT);
                amm.setVerticalAlignment(Element.ALIGN_CENTER);
                table2.addCell(amm);

                Paragraph prepared = new Paragraph("Prepared By :___________         Approved By :___________         Received By :___________");
                prepared.setSpacingBefore(35);
                prepared.setAlignment(Element.ALIGN_CENTER);

                Paragraph line = new Paragraph("_____________________________________________________________________________");
                line.setSpacingBefore(15);
                line.setAlignment(Element.ALIGN_CENTER);

                PdfPTable footer = new PdfPTable(3);
                footer.setWidthPercentage(100);
                footer.setSpacingBefore(5);
                Date dat = new Date();
                SimpleDateFormat df = new SimpleDateFormat("EEE, d MMM yyyy");
                footer.addCell(getCell(df.format(dat), PdfPCell.ALIGN_LEFT));
                footer.addCell(getCell(" ", PdfPCell.ALIGN_CENTER));
                footer.addCell(getCell("Page 1 of 1", PdfPCell.ALIGN_RIGHT));

                Font linkFont = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD);
                Paragraph link = new Paragraph("Software Developed By : www.easysoft.com.pk",linkFont);
                link.setSpacingBefore(15);
                link.setAlignment(Element.ALIGN_CENTER);

                PdfWriter.getInstance(document, output);
                document.open();//PDF document opened........


                document.add(chunk);
                document.add(name);
                document.add(contact);
                document.add(table);
                document.add(billTable);
                document.add(table1);
                document.add(table2);
                document.add(prepared);
                document.add(line);
                document.add(footer);
                document.add(link);

                document.close();

//            file.close();

                System.out.println("Pdf created successfully..");
                Log.e("INVOICE","Pdf created successfully..");
                customPDFView();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    public PdfPCell getCell(String text, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text));
        cell.setPadding(0);
        cell.setHorizontalAlignment(alignment);
        cell.setBorder(PdfPCell.NO_BORDER);
        return cell;
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
            return result.replaceAll("^\\s+", "").replaceAll("\\b\\s{2,}\\b", " ");
        }

    }


}
