package com.fetch.mobile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class MainActivity extends Activity {
    private static final int PAPER = Color.rgb(247, 248, 244);
    private static final int INK = Color.rgb(21, 28, 32);
    private static final int MUTED = Color.rgb(114, 128, 134);
    private static final int GREEN = Color.rgb(25, 61, 49);
    private static final int LIME = Color.rgb(199, 247, 91);
    private static final int PURPLE = Color.rgb(121, 103, 240);
    private static final int ORANGE = Color.rgb(253, 141, 90);
    private static final int LINE = Color.rgb(228, 232, 229);
    private static final String PREFS = "linked_accounts";

    private final List<SearchResult> searchResults = Arrays.asList(
            new SearchResult("Dune: Part Two (2024) 2160p BluRay REMUX", "71.4 GB", "1,248 seeders", "TORRENT"),
            new SearchResult("Dune Part Two 2024 2160p UHD HDR x265", "26.8 GB", "1,104 days", "SCENENZBS"),
            new SearchResult("Dune Part Two (2024) 1080p BluRay DTS", "15.2 GB", "862 seeders", "TORRENT"),
            new SearchResult("Shōgun S01 Complete 1080p WEB-DL", "24.5 GB", "821 days", "SCENENZBS"));
    private final List<Download> downloads = Arrays.asList(
            new Download("Dune Part Two", "4K · 18.6 GB · Torrent", "64%", ORANGE),
            new Download("Shōgun · S01E08", "1080P · 2.4 GB · NZB", "READY", PURPLE),
            new Download("The Bear · S03", "1080P · 12.8 GB · Torrent", "READY", PURPLE));
    private LinearLayout content;
    private LinearLayout navigation;
    private SharedPreferences preferences;
    private int selectedTab;

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        preferences = getSharedPreferences(PREFS, MODE_PRIVATE);
        showHome();
    }

    private void showHome() {
        selectedTab = 0;
        beginScreen("DOWNLOAD MANAGER", "Fetch");
        LinearLayout hero = column(16, GREEN, 22);
        hero.addView(rowBetween(label("TORBOX CLOUD", 11, Color.rgb(180, 213, 197), true), chip("●  CONNECTED", LIME, Color.rgb(16, 44, 36))));
        hero.addView(space(15));
        hero.addView(label("68.4 GB", 35, Color.WHITE, true));
        hero.addView(label("of 100 GB used", 12, Color.rgb(168, 194, 184), false));
        hero.addView(space(16));
        hero.addView(label("11 downloads   •   1 active", 12, Color.rgb(190, 208, 202), false));
        content.addView(hero, matchWrap(0, 16));

        content.addView(section("UNIFIED INDEX", "Find anything", "2 SOURCES"), matchWrap(0, 23));
        content.addView(searchButton(), matchWrap(0, 12));
        LinearLayout filters = row();
        filters.addView(filter("ALL", true)); filters.addView(filter("MOVIES", false)); filters.addView(filter("TV", false)); filters.addView(filter("OTHER", false));
        content.addView(filters, matchWrap(0, 12));

        content.addView(section("YOUR CLOUD", "Recent downloads", "VIEW ALL"), matchWrap(0, 23));
        for (Download download : downloads) content.addView(downloadCard(download), matchWrap(0, 9));
        finishScreen();
    }

    private void showSearch(String query) {
        selectedTab = 1;
        beginScreen("UNIFIED INDEX", "Search");
        content.addView(searchButton(), matchWrap(0, 8));
        List<SearchResult> filtered = new ArrayList<>();
        String needle = query.trim().toLowerCase(Locale.US);
        for (SearchResult result : searchResults) if (needle.isEmpty() || result.name.toLowerCase(Locale.US).contains(needle)) filtered.add(result);
        content.addView(label(filtered.size() + " RESULTS", 10, MUTED, true), matchWrap(2, 20));
        if (filtered.isEmpty()) {
            LinearLayout empty = column(20, Color.WHITE, 18);
            empty.addView(label("No matches yet", 18, INK, true));
            empty.addView(label("Try a broader title or search again.", 13, MUTED, false), matchWrap(0, 6));
            content.addView(empty, matchWrap(0, 10));
        } else {
            for (SearchResult result : filtered) content.addView(resultCard(result), matchWrap(0, 9));
        }
        finishScreen();
    }

    private void showDownloads() {
        selectedTab = 2;
        beginScreen("TORBOX CLOUD", "Downloads");
        content.addView(label("3 ITEMS · 1 ACTIVE", 10, MUTED, true), matchWrap(2, 8));
        for (Download download : downloads) content.addView(downloadCard(download), matchWrap(0, 9));
        finishScreen();
    }

    private void showAccounts() {
        selectedTab = 3;
        beginScreen("SETTINGS", "Accounts");
        content.addView(label("LINKED SERVICES", 10, MUTED, true), matchWrap(2, 8));
        content.addView(accountCard("TorBox", "Cloud downloads & storage", GREEN), matchWrap(0, 10));
        content.addView(accountCard("SceneNZBs", "NZB indexer search", PURPLE), matchWrap(0, 10));
        LinearLayout privacy = column(16, Color.rgb(232, 241, 235), 17);
        privacy.addView(label("PRIVATE BY DEFAULT", 10, Color.rgb(82, 112, 96), true));
        privacy.addView(label("Tokens stay in this app's private storage. Android backup is disabled.", 13, Color.rgb(53, 86, 72), false), matchWrap(0, 8));
        content.addView(privacy, matchWrap(0, 22));
        finishScreen();
    }

    private void beginScreen(String eyebrow, String title) {
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(PAPER);
        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);
        content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(dp(20), dp(18), dp(20), dp(18));
        content.addView(label(eyebrow, 10, MUTED, true));
        content.addView(label(title, 29, INK, true), matchWrap(0, 3));
        scroll.addView(content, new ScrollView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        root.addView(scroll, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));
        navigation = row();
        navigation.setPadding(dp(5), dp(7), dp(5), dp(7));
        navigation.setBackgroundColor(Color.WHITE);
        root.addView(navigation, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(70)));
        setContentView(root);
    }

    private void finishScreen() {
        content.addView(space(8));
        addNav("⌂", "HOME", 0); addNav("⌕", "SEARCH", 1); addNav("↓", "DOWNLOADS", 2); addNav("♙", "ACCOUNT", 3);
    }

    private void addNav(String icon, String title, final int tab) {
        LinearLayout item = new LinearLayout(this);
        item.setOrientation(LinearLayout.VERTICAL); item.setGravity(Gravity.CENTER); item.setPadding(2, 2, 2, 2);
        int color = selectedTab == tab ? Color.rgb(51, 88, 72) : Color.rgb(154, 164, 163);
        item.addView(label(icon, 22, color, true)); item.addView(label(title, 9, color, true));
        item.setContentDescription(title); item.setClickable(true); item.setOnClickListener(v -> selectTab(tab));
        navigation.addView(item, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1));
    }

    private void selectTab(int tab) {
        if (tab == 0) showHome(); else if (tab == 1) showSearch(""); else if (tab == 2) showDownloads(); else showAccounts();
    }

    private View searchButton() {
        Button button = new Button(this);
        button.setText("⌕   Search torrents & NZBs"); button.setTextSize(13); button.setTextColor(MUTED); button.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
        button.setAllCaps(false); button.setPadding(dp(14), 0, dp(14), 0); button.setBackground(shape(Color.WHITE, 16, LINE));
        button.setContentDescription("Search torrents and NZBs"); button.setOnClickListener(v -> askForSearch());
        button.setMinHeight(dp(54)); return button;
    }

    private void askForSearch() {
        EditText input = new EditText(this);
        input.setHint("Movie, show, or release name"); input.setSingleLine(true); input.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        AlertDialog dialog = new AlertDialog.Builder(this).setTitle("Find anything").setMessage("Search available torrent and NZB sources.").setView(input)
                .setPositiveButton("Search", (d, which) -> showSearch(input.getText().toString())).setNegativeButton("Cancel", null).create();
        input.setOnEditorActionListener((v, action, event) -> { if (action == EditorInfo.IME_ACTION_SEARCH) { dialog.dismiss(); showSearch(input.getText().toString()); return true; } return false; });
        dialog.show(); input.requestFocus();
    }

    private View resultCard(SearchResult result) {
        LinearLayout card = row(); card.setGravity(Gravity.CENTER_VERTICAL); card.setPadding(dp(12), dp(12), dp(9), dp(12)); card.setBackground(shape(Color.WHITE, 16, LINE));
        TextView badge = label(result.type.equals("TORRENT") ? "T" : "N", 15, Color.WHITE, true); badge.setGravity(Gravity.CENTER); badge.setBackground(shape(result.type.equals("TORRENT") ? ORANGE : PURPLE, 11, 0)); card.addView(badge, new LinearLayout.LayoutParams(dp(42), dp(42)));
        LinearLayout copy = column(); copy.setPadding(dp(11), 0, dp(5), 0); copy.addView(label(result.name, 13, INK, true)); copy.addView(label(result.type + "   •   " + result.size + "   •   " + result.detail, 10, MUTED, false), matchWrap(0, 6)); card.addView(copy, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        Button add = compactButton("+", Color.rgb(237, 246, 212), Color.rgb(82, 114, 51)); add.setContentDescription("Add " + result.name + " to TorBox"); add.setOnClickListener(v -> Toast.makeText(this, "Added to TorBox", Toast.LENGTH_SHORT).show()); card.addView(add, new LinearLayout.LayoutParams(dp(46), dp(46)));
        return card;
    }

    private View downloadCard(Download download) {
        LinearLayout card = row(); card.setGravity(Gravity.CENTER_VERTICAL); card.setPadding(dp(12), dp(12), dp(12), dp(12)); card.setBackground(shape(Color.WHITE, 16, LINE));
        TextView badge = label("▶", 15, Color.WHITE, true); badge.setGravity(Gravity.CENTER); badge.setBackground(shape(download.color, 13, 0)); card.addView(badge, new LinearLayout.LayoutParams(dp(48), dp(48)));
        LinearLayout copy = column(); copy.setPadding(dp(12), 0, dp(7), 0); copy.addView(label(download.name, 14, INK, true)); copy.addView(label(download.meta, 11, MUTED, false), matchWrap(0, 6)); card.addView(copy, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        card.addView(label(download.status, 10, download.status.equals("READY") ? Color.rgb(83, 129, 105) : Color.rgb(98, 129, 71), true));
        return card;
    }

    private View accountCard(final String service, String subtitle, int color) {
        LinearLayout card = row(); card.setGravity(Gravity.CENTER_VERTICAL); card.setPadding(dp(12), dp(12), dp(12), dp(12)); card.setBackground(shape(Color.WHITE, 16, LINE));
        TextView badge = label(service.substring(0, 1), 18, Color.WHITE, true); badge.setGravity(Gravity.CENTER); badge.setBackground(shape(color, 13, 0)); card.addView(badge, new LinearLayout.LayoutParams(dp(48), dp(48)));
        LinearLayout copy = column(); copy.setPadding(dp(12), 0, dp(5), 0); copy.addView(label(service, 14, INK, true)); copy.addView(label(subtitle, 11, MUTED, false), matchWrap(0, 5)); card.addView(copy, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        boolean linked = preferences.contains(service); Button action = compactButton(linked ? "LINKED" : "LINK", linked ? Color.rgb(234, 244, 237) : Color.rgb(237, 246, 212), linked ? Color.rgb(83, 129, 105) : Color.rgb(82, 114, 51)); action.setOnClickListener(v -> manageAccount(service)); card.addView(action);
        return card;
    }

    private void manageAccount(String service) {
        if (!preferences.contains(service)) { linkAccount(service); return; }
        new AlertDialog.Builder(this).setTitle(service + " account").setMessage("This account is linked on this device.")
                .setPositiveButton("Keep linked", null).setNegativeButton("Unlink", (dialog, which) -> { preferences.edit().remove(service).apply(); showAccounts(); Toast.makeText(this, service + " unlinked", Toast.LENGTH_SHORT).show(); }).show();
    }

    private void linkAccount(String service) {
        EditText input = new EditText(this); input.setHint(service + " API token"); input.setSingleLine(true); input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        new AlertDialog.Builder(this).setTitle("Link " + service).setMessage("Paste your API token. It stays in this app's private storage.").setView(input)
                .setPositiveButton("Link account", (dialog, which) -> { String token = input.getText().toString().trim(); if (token.isEmpty()) { Toast.makeText(this, "Enter an API token", Toast.LENGTH_SHORT).show(); return; } preferences.edit().putString(service, token).apply(); showAccounts(); Toast.makeText(this, service + " linked", Toast.LENGTH_SHORT).show(); })
                .setNegativeButton("Cancel", null).show();
    }

    private LinearLayout section(String eyebrow, String title, String action) { LinearLayout section = rowBetween(column(), label(action, 10, Color.rgb(79, 113, 101), true)); LinearLayout copy = (LinearLayout) section.getChildAt(0); copy.addView(label(eyebrow, 10, MUTED, true)); copy.addView(label(title, 22, INK, true), matchWrap(0, 4)); return section; }
    private TextView filter(String text, boolean active) { TextView view = label(text, 10, active ? Color.WHITE : MUTED, true); view.setGravity(Gravity.CENTER); view.setBackground(shape(active ? Color.rgb(37, 59, 53) : Color.WHITE, 15, LINE)); view.setPadding(dp(12), dp(8), dp(12), dp(8)); LinearLayout.LayoutParams params = wrap(); params.rightMargin = dp(7); view.setLayoutParams(params); return view; }
    private TextView chip(String text, int foreground, int background) { TextView chip = label(text, 9, foreground, true); chip.setPadding(dp(9), dp(6), dp(9), dp(6)); chip.setBackground(shape(background, 13, 0)); return chip; }
    private Button compactButton(String text, int background, int foreground) { Button button = new Button(this); button.setText(text); button.setTextSize(11); button.setTextColor(foreground); button.setTypeface(Typeface.DEFAULT_BOLD); button.setAllCaps(false); button.setMinHeight(dp(42)); button.setMinWidth(dp(52)); button.setPadding(dp(7), 0, dp(7), 0); button.setBackground(shape(background, 14, 0)); return button; }
    private TextView label(String text, float size, int color, boolean bold) { TextView view = new TextView(this); view.setText(text); view.setTextSize(size); view.setTextColor(color); view.setTypeface(bold ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT); return view; }
    private LinearLayout row() { LinearLayout row = new LinearLayout(this); row.setOrientation(LinearLayout.HORIZONTAL); return row; }
    private LinearLayout rowBetween(View left, View right) { LinearLayout row = row(); row.setGravity(Gravity.CENTER_VERTICAL); row.addView(left, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1)); row.addView(right); return row; }
    private LinearLayout column() { LinearLayout column = new LinearLayout(this); column.setOrientation(LinearLayout.VERTICAL); return column; }
    private LinearLayout column(int padding, int color, int radius) { LinearLayout column = column(); column.setPadding(dp(padding), dp(padding), dp(padding), dp(padding)); column.setBackground(shape(color, radius, 0)); return column; }
    private Space space(int height) { Space space = new Space(this); space.setLayoutParams(new LinearLayout.LayoutParams(1, dp(height))); return space; }
    private GradientDrawable shape(int color, int radius, int stroke) { GradientDrawable drawable = new GradientDrawable(); drawable.setColor(color); drawable.setCornerRadius(dp(radius)); if (stroke != 0) drawable.setStroke(dp(1), stroke); return drawable; }
    private LinearLayout.LayoutParams wrap() { return new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT); }
    private LinearLayout.LayoutParams matchWrap(int top, int bottom) { LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT); params.topMargin = dp(top); params.bottomMargin = dp(bottom); return params; }
    private int dp(int value) { return Math.round(value * getResources().getDisplayMetrics().density); }

    private static final class SearchResult { final String name, size, detail, type; SearchResult(String name, String size, String detail, String type) { this.name = name; this.size = size; this.detail = detail; this.type = type; } }
    private static final class Download { final String name, meta, status; final int color; Download(String name, String meta, String status, int color) { this.name = name; this.meta = meta; this.status = status; this.color = color; } }
}
