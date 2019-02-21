package org.by9steps.shadihall.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.by9steps.shadihall.R;
import org.by9steps.shadihall.bean.Dir;
import org.by9steps.shadihall.viewbinder.DirectoryNodeBinder;
import org.by9steps.shadihall.viewbinder.FileNodeBinder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import tellh.com.recyclertreeview_lib.TreeNode;
import tellh.com.recyclertreeview_lib.TreeViewAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class TreeFragment extends Fragment {

    private RecyclerView rv;
    private TreeViewAdapter adapter;

    List<String> country = new ArrayList<>();
    List<String> city = new ArrayList<>();
    List<String> town = new ArrayList<>();
    List<String> community = new ArrayList<>();


    public TreeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tree, container, false);

        rv = view.findViewById(R.id.rv);

        country.add("Pakistan");

        city.add( "Karachi");
        city.add("Lahore");
        city.add("Islamabad");
        city.add("Faisalabad");

        town.add("Saddar Town");
        town.add("Malir Town");
        town.add("New Karachi Town");

        community.add("Bussiness");
        community.add("Education");
        community.add("Sports");

        initData();

        return view;
    }


    private void initData() {
        List<TreeNode> nodes = new ArrayList<>();

        TreeNode<Dir> app = new TreeNode<>(new Dir("1","Pakistan"));
        nodes.add(app);
        TreeNode<Dir> cit = null;
        TreeNode<Dir> ton;
        TreeNode<Dir> com;

        for (String s : city){
            cit = new TreeNode<>(new Dir("2",s));
            app.addChild(cit);
            for (String n : town){
                ton = new TreeNode<>(new Dir("3",n));
                cit.addChild(ton);
                for (String t : community){
                    com = new TreeNode<>(new Dir("4",t));
                    ton.addChild(com);
                }
            }
        }

        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new TreeViewAdapter(nodes, Arrays.asList(new FileNodeBinder(getContext()), new DirectoryNodeBinder(getContext())));
        // whether collapse child nodes when their parent node was close.
//        adapter.ifCollapseChildWhileCollapseParent(true);
        adapter.setOnTreeNodeListener(new TreeViewAdapter.OnTreeNodeListener() {
            @Override
            public boolean onClick(TreeNode node, RecyclerView.ViewHolder holder) {
                if (!node.isLeaf()) {
                    //Update and toggle the node.
                    onToggle(!node.isExpand(), holder);
//                    if (!node.isExpand())
//                        adapter.collapseBrotherNode(node);
                }
                return false;
            }

            @Override
            public void onToggle(boolean isExpand, RecyclerView.ViewHolder holder) {
                DirectoryNodeBinder.ViewHolder dirViewHolder = (DirectoryNodeBinder.ViewHolder) holder;
                final ImageView ivArrow = dirViewHolder.getIvArrow();
                int rotateDegree = isExpand ? 90 : -90;
                ivArrow.animate().rotationBy(rotateDegree)
                        .start();
            }
        });
        rv.setAdapter(adapter);
    }

}
