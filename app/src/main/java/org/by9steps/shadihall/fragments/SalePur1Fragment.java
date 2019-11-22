package org.by9steps.shadihall.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
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
import com.itextpdf.text.Paragraph;
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

import org.by9steps.shadihall.BuildConfig;
import org.by9steps.shadihall.R;
import org.by9steps.shadihall.activities.MenuClickActivity;
import org.by9steps.shadihall.activities.Salepur1AddNewActivity;
import org.by9steps.shadihall.fragments.salepurviewtypes.salepurgridviewfrag;
import org.by9steps.shadihall.helper.DatabaseHelper;
import org.by9steps.shadihall.helper.MNotificationClass;
import org.by9steps.shadihall.model.JoinQueryDaliyEntryPage1;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SalePur1Fragment extends Fragment implements AdapterView.OnItemSelectedListener,
        View.OnClickListener {

    private DatabaseHelper databaseHelper;
    ///////////////Fragment View Related Compnents
    private Spinner spinner;
    private SearchView searchView;
    private ImageView addnew;
    private String EntryType = null;
    //Print
    private File pdfFile;
    Button viewgrid, viewtree, other;
    List<JoinQueryDaliyEntryPage1> listForPdf;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        listForPdf = salepurgridviewfrag.page1List;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sale_pur1, container, false);
        AssignIdsToViewWidget(view);
        SetItemOnSpinner();
        spinner.setOnItemSelectedListener(this);
        EntryType = getArguments().getString("EntryType");
        ShowGridData();
        return view;
    }

    private void AssignIdsToViewWidget(View vv) {

        databaseHelper = new DatabaseHelper(getContext());


        spinner = vv.findViewById(R.id.salpur_spinner);
        addnew = vv.findViewById(R.id.salpur_add);
        addnew.setOnClickListener(this);
        searchView = vv.findViewById(R.id.salpur_search);


        viewgrid = vv.findViewById(R.id.btnhorizontal1);
        viewgrid.setOnClickListener(this);
        viewtree = vv.findViewById(R.id.btnhorizontal2);
        viewtree.setOnClickListener(this);
        other = vv.findViewById(R.id.btnhorizontal3);
        other.setOnClickListener(this);


    }

    public void ShowGridData() {
        MNotificationClass.ShowToastTem(getContext(), "Grid View ");
        Bundle bundle = new Bundle();
        bundle.putString("EntryType", EntryType);
        salepurgridviewfrag salepurgridviewfrag = new salepurgridviewfrag();
        salepurgridviewfrag.setArguments(bundle);
        getChildFragmentManager().beginTransaction()
                .add(R.id.containersalepur1, salepurgridviewfrag)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.cb_menu, menu);
        MenuItem settings = menu.findItem(R.id.action_settings);
        settings.setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getActivity().onBackPressed();
        } else if (item.getItemId() == R.id.action_print) {
            try {
                createPdf();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (DocumentException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return true;
        } else if (item.getItemId() == R.id.action_refresh) {
//            if (isConnected()) {
//                refereshTables(getContext());
//            } else {
//                Toast.makeText(getContext(), "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
//            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        MNotificationClass.ShowToastTem(getContext(), position + "");
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        if (R.id.salpur_add == v.getId()) {
            Intent intent = new Intent(getContext(), Salepur1AddNewActivity.class);
            intent.putExtra("EntryType", EntryType);
            intent.putExtra("edit", false);
            startActivity(intent);
        }
        switch (v.getId()) {
            case R.id.btnhorizontal1:
                ShowGridData();
                break;
            case R.id.btnhorizontal2:
                MNotificationClass.ShowToastTem(getContext(), "Tree View ");

                break;
            case R.id.btnhorizontal3:
                MNotificationClass.ShowToastTem(getContext(), "other View ");

                break;
        }
    }

    //////////////////////SetItemOnSpinner
    public void SetItemOnSpinner() {
        List<String> spinner_list = new ArrayList<String>();
        spinner_list.add("Select");
        spinner_list.add("No");
        spinner_list.add("Date");
        spinner_list.add("AccountName");
        spinner_list.add("Remarks");
        spinner_list.add("BillAmount");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, spinner_list);
        spinner.setAdapter(dataAdapter);
    }

    @Override
    public void onStop() {
        super.onStop();
        databaseHelper.close();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        databaseHelper.close();
    }

    //////////////////////////////////All Pdf Fun Goes here
    public void createPdf() throws IOException, DocumentException {

        File docsFolder = new File(Environment.getExternalStorageDirectory() + "/Documents");
        if (!docsFolder.exists()) {
            docsFolder.mkdir();
        }
        String pdfname = "SalePur1.pdf";
        pdfFile = new File(docsFolder.getAbsolutePath(), pdfname);
        OutputStream output = new FileOutputStream(pdfFile);

        Document document = new Document(PageSize.A4);
        PdfWriter writer = PdfWriter.getInstance(document, output);
        writer.createXmpMetadata();
        writer.setTagged();
        writer.setPageEvent(new Footer(this.getContext()));
        document.open();
        document.addLanguage("en-us");

        PdfDictionary parameters = new PdfDictionary();
        Log.e("PDFDocument", "Created2");
        parameters.put(PdfName.MODDATE, new PdfDate());

        Font chapterFont = FontFactory.getFont(FontFactory.HELVETICA, 16, Font.BOLD);
        Font paragraphFont = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD);
        Chunk chunk = new Chunk("Client Name:Umer Bilal For Testing Pdf", chapterFont);
        Paragraph name = new Paragraph("Address:This is Sample Adddress For Testing", paragraphFont);
        name.setIndentationLeft(0);
        Paragraph contact = new Paragraph("Contact:034151", paragraphFont);
        contact.setIndentationLeft(0);

        PdfPTable title = new PdfPTable(new float[]{3, 3, 3});
        title.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        title.getDefaultCell().setFixedHeight(30);
        title.setTotalWidth(PageSize.A4.getWidth());
        title.setWidthPercentage(100);
        title.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        title.setSpacingBefore(5);
        title.addCell(footerCell("", PdfPCell.ALIGN_CENTER));
        PdfPCell cell = new PdfPCell(new Phrase(((MenuClickActivity) getActivity()).getSupportActionBar().getTitle().toString() + " Invoice", chapterFont));
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        title.addCell(cell);
        title.addCell(footerCell("", PdfPCell.ALIGN_CENTER));

        title.addCell(footerCell("", PdfPCell.ALIGN_CENTER));
        title.addCell(footerCell("", PdfPCell.ALIGN_CENTER));

        Date date = new Date();
        String date1 = new SimpleDateFormat("yyyy-MM-dd").format(date);
        PdfPCell pCell = pCell = new PdfPCell(new Phrase("Date" + ": " + date1));
        pCell.setBorder(PdfPCell.NO_BORDER);
        pCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        pCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        title.addCell(pCell);

        PdfPTable table = new PdfPTable(new float[]{1, 1, 1, 1, 1});
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.getDefaultCell().setFixedHeight(40);
        //table.setTotalWidth(PageSize.A4.getWidth());
        table.setWidthPercentage(100);
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.setSpacingBefore(20);
        table.addCell("No");
        table.addCell("Date");
        table.addCell("AccountName");
        table.addCell("Remarks");
        table.addCell("BillAmount");

        // table.setHeaderRows(1);
        PdfPCell[] cells = table.getRow(0).getCells();
        for (int j = 0; j < cells.length; j++) {
            cells[j].setBackgroundColor(BaseColor.PINK);
        }
        int dTotal = 0, cTotal = 0;
        listForPdf = salepurgridviewfrag.page1List;
        if (listForPdf.size() > 0) {
            int i = 0;
            for (JoinQueryDaliyEntryPage1 c : listForPdf) {
                table.addCell(getCell(c.getSalePur1ID(), PdfPCell.ALIGN_LEFT, i));
                table.addCell(getCell(c.getSPDate(), PdfPCell.ALIGN_RIGHT, i));
                table.addCell(getCell(c.getAcName(), PdfPCell.ALIGN_RIGHT, i));
                table.addCell(getCell(c.getRemarks(), PdfPCell.ALIGN_RIGHT, i));
                table.addCell(getCell(c.getBillAmt(), PdfPCell.ALIGN_RIGHT, i));
                i++;
                ///////////////////////////////My Aditeion
//                if (c.getDebitBal() == null || c.getDebitBal().isEmpty()) {
//                    dTotal = dTotal + 0;
//                } else
//                    dTotal = dTotal + Integer.parseInt(c.getDebitBal());
//                if (c.getCreditBal() == null || c.getCreditBal().isEmpty()) {
//                    cTotal = cTotal + 0;
//                } else
//                    cTotal = cTotal + Integer.parseInt(c.getCreditBal());
//                ////////////////////////////////////
////                dTotal = dTotal + Integer.valueOf(c.getDebitBal());
////                cTotal = cTotal + Integer.valueOf(c.getCreditBal());
            }
        }
//        else {
//            for (Reports c : reportsList) {
//                table.addCell(getCell(c.getAcName(), PdfPCell.ALIGN_LEFT));
//                table.addCell(getCell(c.getDebitBal(), PdfPCell.ALIGN_RIGHT));
//                table.addCell(getCell(c.getCreditBal(), PdfPCell.ALIGN_RIGHT));
//                ///////////////////////////////My Aditeion
//                if (c.getDebitBal() == null || c.getDebitBal().isEmpty()) {
//                    dTotal = dTotal + 0;
//                } else
//                    dTotal = dTotal + Integer.parseInt(c.getDebitBal());
//                if (c.getCreditBal() == null || c.getCreditBal().isEmpty()) {
//                    cTotal = cTotal + 0;
//                } else
//                    cTotal = cTotal + Integer.parseInt(c.getCreditBal());
//                ////////////////////////////////////
////                dTotal = dTotal + Integer.valueOf(c.getDebitBal());
////                cTotal = cTotal + Integer.valueOf(c.getCreditBal());
//            }
//        }

        Font totalFont = FontFactory.getFont(FontFactory.HELVETICA, 13, Font.BOLD);
        PdfPCell total = new PdfPCell(new Phrase("Total", totalFont));
        total.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        total.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(total);
        table.addCell(getCell(String.valueOf("0"), PdfPCell.ALIGN_RIGHT, -1));
        table.addCell(getCell(String.valueOf("0"), PdfPCell.ALIGN_RIGHT, -1));
        table.addCell(getCell(String.valueOf("0"), PdfPCell.ALIGN_RIGHT, -1));
        table.addCell(getCell(String.valueOf("0"), PdfPCell.ALIGN_RIGHT, -1));
        table.addCell(getCell(String.valueOf("0"), PdfPCell.ALIGN_RIGHT, -1));

        document.open();

        Font f = new Font(Font.FontFamily.TIMES_ROMAN, 30.0f, Font.UNDERLINE, BaseColor.BLACK);
        Paragraph paragraph = new Paragraph("Cash Book \n\n", f);
        paragraph.setAlignment(Element.ALIGN_CENTER);
        document.add(chunk);
        document.add(name);
        document.add(contact);
        document.add(title);
        document.add(table);
//        try {
//            Drawable d = this.getResources().getDrawable(R.drawable.supplier);
//
//            BitmapDrawable bitDw = ((BitmapDrawable) d);
//            Bitmap bmp = bitDw.getBitmap();
//            ByteArrayOutputStream stream = new ByteArrayOutputStream();
//            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
//            Image image = Image.getInstance(stream.toByteArray());
//            image.setWidthPercentage(10f);
//            image.scaleToFit(80,80);
//            document.add(image);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        //document.add(paragraph);

        document.close();
        customPDFView();
        Log.e("PDFDocument", "Created");
    }

    public PdfPCell footerCell(String text, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text));
        cell.setPadding(0);
        cell.setHorizontalAlignment(alignment);
        cell.setBorder(PdfPCell.NO_BORDER);
        return cell;
    }

    public PdfPCell getCell(String text, int alignment, int loc) {
        PdfPCell cell = new PdfPCell(new Phrase(text));
        cell.setPadding(0);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(alignment);
        cell.setFixedHeight(40);
        cell.setPadding(3);
        //cell.setBackgroundColor(new BaseColor(Color.CYAN));
        if (loc != -1 && loc % 2 == 0) {

        } else
            cell.setBackgroundColor(BaseColor.WHITE);
        return cell;
    }

    class Footer extends PdfPageEventHelper {
        Font font;
        PdfTemplate t;
        Image total;
        Context context;

        public Footer(Context context) {
            this.context = context;
        }

        @Override
        public void onOpenDocument(PdfWriter writer, Document document) {
            t = writer.getDirectContent().createTemplate(30, 16);
            try {
                total = Image.getInstance(t);
                total.setRole(PdfName.ARTIFACT);
                font = new Font(Font.FontFamily.TIMES_ROMAN, 30.0f, Font.UNDERLINE, BaseColor.BLACK);
            } catch (DocumentException de) {
                throw new ExceptionConverter(de);
            }
        }

        @Override
        public void onEndPage(PdfWriter writer, Document document) {

            PdfPTable table = new PdfPTable(new float[]{3, 4, 2});
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            table.getDefaultCell().setFixedHeight(10);
            table.setTotalWidth(PageSize.A4.getWidth());
            table.getDefaultCell().setBorder(Rectangle.BOTTOM);
            Date dat = new Date();
            SimpleDateFormat df = new SimpleDateFormat("EEE, d MMM yyyy");
            table.addCell(footerCell(df.format(dat), PdfPCell.ALIGN_LEFT));
            table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));
            Log.e("PAGE NUMBER", String.valueOf(writer.getPageNumber()));
            table.addCell(footerCell(String.format("Page %d ", writer.getPageNumber() - 1), PdfPCell.ALIGN_LEFT));
            try {

                Drawable d = context.getResources().getDrawable(R.drawable.supplier);
                BitmapDrawable bitDw = ((BitmapDrawable) d);
                Bitmap bmp = bitDw.getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                Image image = Image.getInstance(stream.toByteArray());
                //image.setWidthPercentage(10f);
                image.scaleToFit(80, 80);
                PdfPCell cell=new PdfPCell();
                cell.setFixedHeight(10);
                cell.setImage(image);
                cell.setBorder(Rectangle.NO_BORDER);
                // document.add(image);
                table.addCell(cell);
            } catch (Exception e) {
                table.addCell(footerCell("LeftBlank", PdfPCell.ALIGN_LEFT));

                e.printStackTrace();
            }
            table.addCell(footerCell("www.easysoft.com.pk", PdfPCell.ALIGN_LEFT));

                table.addCell(footerCell("LeftBlank", PdfPCell.ALIGN_LEFT));



/////////////////////////////////////////////

            ///////////////////////////////////////
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

    public void customPDFView() {
        try {
            PackageManager packageManager = getContext().getPackageManager();

            Intent testIntent = new Intent(Intent.ACTION_VIEW);
            testIntent.setType("application/pdf");
            List list = packageManager.queryIntentActivities(testIntent, PackageManager.MATCH_DEFAULT_ONLY);
            if (list.size() > 0) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);

                Uri uri = FileProvider.getUriForFile(getContext(), BuildConfig.APPLICATION_ID + ".provider", pdfFile.getAbsoluteFile());


                // Uri uri = Uri.fromFile(pdfFile);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setDataAndType(uri, "application/pdf");
                getContext().startActivity(intent);

            } else {
                Toast.makeText(getContext(), "Download a PDF Viewer to see the generated PDF", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            MNotificationClass.ShowToastTem(getContext(), "Error While Openin File Check File in " + pdfFile.getAbsolutePath());

            e.printStackTrace();

        }
    }
}
