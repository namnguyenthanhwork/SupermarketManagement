package com.ou.controllers;

import com.ou.pojos.SalePercent;
import com.ou.services.SalePercentService;
import com.ou.utils.AlertUtils;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class SalePercentController implements Initializable {
    private static final SalePercentService SALE_PERCENT_SERVICE;

    static {
        SALE_PERCENT_SERVICE = new SalePercentService();
    }

    @FXML
    private TableView<SalePercent> tbvSper;

    @FXML
    private TextField txtSperId;

    @FXML
    private TextField txtSperPercent;

    @FXML
    private TextField txtSperIsActive;

    @FXML
    private TextField txtSearchSper;

    @FXML
    private Text textSperAmount;

    @FXML
    private Button btnAddSper;

    @FXML
    private Button btnDelSper;

    @FXML
    private Button btnBack;

    @FXML
    private Button btnEditSper;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.initInputData();
        this.initSperTbv();
        this.loadSperTbvColumns();
        this.loadSperTbvData(-1);
        this.loadSperAmount();
        this.tbvSper.getSelectionModel().getSelectedItems()
                .addListener((ListChangeListener<? super SalePercent>) e -> changeInputData());
        this.btnAddSper.setOnMouseClicked(e -> addSalePercent());
        this.btnEditSper.setOnMouseClicked(e -> updateSalePercent());
        this.btnDelSper.setOnMouseClicked(e -> deleteSalePercent());
        this.btnBack.setOnMouseClicked(e -> backMenu());
        this.txtSearchSper.textProperty().addListener(e -> {
            if (this.txtSearchSper.getText() == null || this.txtSearchSper.getText().length() > 0 )
                loadSperTbvData(Integer.parseInt(this.txtSearchSper.getText()));
            else
                loadSperTbvData(-1);
        });
    }


    // Khỏi tạo các thuộc tính của vùng input
    private void initInputData(){
        this.txtSperId.setDisable(true);
        this.txtSperIsActive.setDisable(true);
    }

    // Khỏi tạo các thuộc tính của table view mã giảm giá
    private void initSperTbv() {
        this.tbvSper.setPlaceholder(new Label("Không có mã giảm giá nào!"));
        this.tbvSper.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    // Lấy dữ liệu cho table view
    private void loadSperTbvData(int kw) {
        try {
            this.tbvSper.setItems(FXCollections.observableList(SALE_PERCENT_SERVICE.getSalePercents(kw)));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Tạo các cột và match dữ liệu cho table view
    private void loadSperTbvColumns(){
        TableColumn<SalePercent, Integer> sperIdColumn = new TableColumn<>("Mã phiếu giảm giá");
        TableColumn<SalePercent, Float> sperPercentColumn = new TableColumn<>("Phần trăm giảm giá");
        TableColumn<SalePercent, Boolean> sperIsActiveColumn = new TableColumn<>("Trạng thái");
        sperIdColumn.setCellValueFactory(new PropertyValueFactory<>("sperId"));
        sperPercentColumn.setCellValueFactory(new PropertyValueFactory<>("sperPercent"));
        sperIsActiveColumn.setCellValueFactory(new PropertyValueFactory<>("sperIsActive"));
        sperIdColumn.setPrefWidth(500);
        sperPercentColumn.setPrefWidth(500);
        sperIsActiveColumn.setPrefWidth(500);
        sperIdColumn.setSortType(TableColumn.SortType.DESCENDING);
        this.tbvSper.getColumns().addAll(sperIdColumn, sperPercentColumn,sperIsActiveColumn);
    }

    // Lấy số lượng mã giảm giá
    private void loadSperAmount(){
            try {
                this.textSperAmount.setText(String.valueOf(SALE_PERCENT_SERVICE.getSalePercentAmount()));
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }

    // Thiết lập vùng dữ liệu input khi lựa chọn thay đổi dưới table view
    private void changeInputData() {
        SalePercent selectedSper = this.tbvSper.getSelectionModel().getSelectedItem();
        if (selectedSper != null) {
            this.txtSperId.setText(String.valueOf(selectedSper.getSperId()));
            this.txtSperPercent.setText(String.valueOf(selectedSper.getSperPercent()));
            this.txtSperIsActive.setText(selectedSper.getSperIsActive()?"Đang hoạt động":"Ngưng hoạt động");
        }
    }

    // Reset dữ liệu vùng input
    private void clearInputData() {
        this.txtSperId.setText("");
        this.txtSperPercent.setText("");
        this.txtSperIsActive.setText("");
    }

    // reload dữ liệu
    private void reloadData() {
        loadSperTbvData(-1);
        loadSperAmount();
        clearInputData();
    }

    // Thêm mã giảm giá mới
    private void addSalePercent() {
        SalePercent salePercent = new SalePercent();
        try {
            salePercent.setSperPercent(Integer.valueOf(this.txtSperPercent.getText()));
            if (SALE_PERCENT_SERVICE.addSalePercent(salePercent)) {
               AlertUtils.showAlert("Thêm thành công", Alert.AlertType.INFORMATION);
               reloadData();
           } else {
               AlertUtils.showAlert("Thêm thất bại !", Alert.AlertType.ERROR);
           }
        }catch (NumberFormatException numberFormatException) {

            AlertUtils.showAlert("Thêm thất bại !", Alert.AlertType.ERROR);
        }

        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Cập nhật thông tin mã giảm giá
    private void updateSalePercent() {
        try {
            SalePercent salePercent = new SalePercent();
            salePercent.setSperId(Integer.parseInt(this.txtSperId.getText()));
            salePercent.setSperPercent(Integer.parseInt(this.txtSperPercent.getText()));
            salePercent.setSperIsActive(this.txtSperIsActive.getText().equals("Đang hoạt động"));

            if (SALE_PERCENT_SERVICE.updateSalePercent(salePercent)) {
                AlertUtils.showAlert("Sửa thành công", Alert.AlertType.INFORMATION);
                reloadData();
            } else {
                AlertUtils.showAlert("Sửa thất bại!", Alert.AlertType.ERROR);
            }
        } catch (NumberFormatException numberFormatException) {
            AlertUtils.showAlert("Sửa thất bại! Vui lòng chọn mã giảm giá để sửa", Alert.AlertType.ERROR);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Xoá mã giảm giá
    private void deleteSalePercent() {
        try {
            SalePercent salePercent = new SalePercent();
            salePercent.setSperId(Integer.parseInt(this.txtSperId.getText()));
            if (SALE_PERCENT_SERVICE.deleteSalePercent(salePercent)) {
                AlertUtils.showAlert("Xoá thành công", Alert.AlertType.INFORMATION);
                reloadData();
            } else {
                AlertUtils.showAlert("Xoá thất bại", Alert.AlertType.ERROR);
            }
        } catch (NumberFormatException numberFormatException) {
            AlertUtils.showAlert("Xoá thất bại! Vui lòng chọn mã giảm giá cần xoá", Alert.AlertType.ERROR);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Trở về giao diện ban đầu
    private void backMenu() {
        // Back to menu here
    }
}
