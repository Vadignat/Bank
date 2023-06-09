package ru.vadignat.ui;

import ru.vadignat.data.Product;
import ru.vadignat.data.Transfer;
import ru.vadignat.data.User;
import ru.vadignat.data.UserVerifier;
import ru.vadignat.net.Client;

import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class AuthWindow extends JFrame {
    private static final int MIN_SZ = GroupLayout.PREFERRED_SIZE;
    private static final int MAX_SZ = GroupLayout.DEFAULT_SIZE;
    private JLabel lblPhone;
    private JLabel lblPassword;
    private JTextField tfPhone;
    private JTextField tfPassword;
    private JTextField tfSum;

    private JButton btnAuth;
    private JButton btnReg;
    private Client client;
    private JLabel lblName;
    private JLabel lblCard;
    private JLabel lblAccount;
    private JLabel lblSum;
    private JLabel lblTransferFrom;
    private JLabel lblTransferTo;
    private JButton btnCard;
    private JButton btnAccount;
    private JButton btnLogOut;
    private JButton btnTransfer;
    private JButton btnSelfTransfer;
    private JButton btnTransferToAnother;
    private JButton btnCancel;
    private JButton btnAdd;
    private JList<String> availableCardList;
    private JList<String> availableAccountList;
    private JScrollPane availableCardPane;
    private JScrollPane availableAccountPane;
    private JScrollPane cardScrollPane;
    private JScrollPane accountScrollPane;

    private GroupLayout gl;
    private ArrayList<Product> cardProducts;
    private ArrayList<Product> accountProducts;
    private List<String> cardNames;
    private List<String> accountNames;

    private Product choosedProduct;
    private JLabel lblProductName;
    private JTextArea txtProductDescription;

    private User user;
    private JList<String> userCardList;
    private JList<String> userAccountList;
    private ArrayList<Product> userCardProducts;
    private ArrayList<Product> userAccountProducts;
    private List<String> userCardNames;
    private List<String> userAccountNames;

    private ActionListener transferActionListener;
    private ActionListener transferActionListener2;
    private ActionListener transferActionListener3;

    private ArrayList<Product> userProducts;
    private JList<String> userProductListFrom;
    private JList<String> userProductListTo;
    private List<String> userProductNames;
    private JScrollPane userProductPaneFrom;
    private JScrollPane userProductPaneTo;

    private MouseAdapter uclmAdapter;
    private MouseAdapter ualmAdapter;
    public AuthWindow(Client client){
        this.client = client;
        client.setWindow(this);
        setSize(600,450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        lblPhone = new JLabel("Номер телефона: ");
        lblPassword = new JLabel("Пароль: ");
        lblSum = new JLabel("Сумма: ");

        tfPhone = new JTextField();
        tfPassword = new JTextField();
        tfSum = new JTextField();

        btnAuth = new JButton("Войти");
        btnReg = new JButton("Регистрация");

        createAuthLayout();

        try {
            client.sendData(4, "");
        } catch (IOException e) {
            showMessage(e.getMessage());
        }


        btnReg.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                var rw = new RegWindow(AuthWindow.this, client);
                rw.setVisible(true);
                setVisible(false);
            }
        });


        btnAuth.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String phone = tfPhone.getText();
                String password = tfPassword.getText();
                UserVerifier userVerifier = new UserVerifier(phone, password);
                try {
                    client.checkUser(userVerifier);
                } catch (IOException ex) {
                    showMessage(ex.getMessage());
                }
            }
        });

        btnAuth.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    btnAuth.doClick();
                }
            }
        });

    }

    public void authorize(User user) {
        this.user = user;
        lblName = new JLabel(user.getLastName() + " " +  user.getFirstName() + " " + user.getMiddleName());
        lblCard = new JLabel("Мои карты");
        lblAccount = new JLabel("Мои счета");
        btnCard = new JButton("+");
        btnAccount = new JButton("+");
        btnTransfer = new JButton("Перевести");
        btnLogOut = new JButton("Выйти");

        btnSelfTransfer = new JButton("Перевод между своими счетами");
        btnTransferToAnother = new JButton("Перевод другому клиенту");
        btnCancel = new JButton("Отмена");

        cardNames = initializeNames(cardProducts, cardNames);
        accountNames = initializeNames(accountProducts, accountNames);

        availableCardList = initializeNamesJList(cardNames);
        availableAccountList = initializeNamesJList(accountNames);

        userProductNames = initializeNames(userProducts, userProductNames);
        userCardNames = initializeNames(userCardProducts, userCardNames);
        userAccountNames = initializeNames(userAccountProducts, userAccountNames);

        userCardList = initializeNamesJList(userCardNames);
        userAccountList = initializeNamesJList(userAccountNames);
        userProductListFrom = initializeNamesJList(userProductNames);
        userProductListTo = initializeNamesJList(userProductNames);

        cardScrollPane = new JScrollPane(userCardList);
        accountScrollPane = new JScrollPane(userAccountList);
        availableCardPane = new JScrollPane(availableCardList);
        availableAccountPane = new JScrollPane(availableAccountList);
        userProductPaneFrom = new JScrollPane(userProductListFrom);
        userProductPaneTo = new JScrollPane(userProductListTo);

        lblTransferFrom = new JLabel("Перевести с:");
        lblTransferTo = new JLabel("Перевести на:");

        lblProductName = new JLabel();
        txtProductDescription = new JTextArea();
        btnAdd = new JButton("Добавить");

        createAuthSuccessLayout();

        DocumentFilter documentFilter = new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                // Проверка, соответствует ли вставляемая строка формату числа с плавающей точкой
                if (isValidFloat(string)) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                // Проверка, соответствует ли заменяемая строка формату числа с плавающей точкой
                if (isValidFloat(text)) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }

            private boolean isValidFloat(String text) {
                // Регулярное выражение для проверки формата числа с плавающей точкой
                String floatRegex = "\\d*(\\.\\d{0,2})?";
                return text.matches(floatRegex);
            }
        };
        AbstractDocument document = (AbstractDocument) tfSum.getDocument();
        document.setDocumentFilter(documentFilter);

        btnLogOut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createAuthLayout();
                tfPhone.setText("");
                tfPassword.setText("");
            }
        });

        transferActionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createChooseTransferLayout();
            }
        };
        btnTransfer.addActionListener(transferActionListener);

        btnTransferToAnother.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createTransferToAnotherLayout();
            }
        });

        btnSelfTransfer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createSelfTransferLayout();
            }
        });

        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createAuthSuccessLayout();
                if(transferActionListener2 != null){
                    btnTransfer.removeActionListener(transferActionListener2);
                }
                if(transferActionListener3 != null){
                    btnTransfer.removeActionListener(transferActionListener3);
                }
                if(transferActionListener != null){
                    btnTransfer.addActionListener(transferActionListener);
                }
                tfSum.setText("");
                tfPhone.setText("");
                try {
                    client.getUserProducts(user);
                } catch (IOException ex) {
                    showMessage(ex.getMessage());
                }
            }
        });

        btnCard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createAddProductLayout(availableCardPane);
            }
        });

        btnAccount.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createAddProductLayout(availableAccountPane);
            }
        });

        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    client.addProduct(user, choosedProduct);
                    if(choosedProduct.getType() == 1) {
                        userCardProducts.add(choosedProduct);
                        userCardNames.add(choosedProduct.getProductName());
                        userCardList = initializeNamesJList(userCardNames);
                        cardScrollPane = new JScrollPane(userCardList);
                        userCardList.addMouseListener(uclmAdapter);
                    }
                    else{
                        userAccountProducts.add(choosedProduct);
                        userAccountNames.add(choosedProduct.getProductName());
                        userAccountList = initializeNamesJList(userAccountNames);
                        accountScrollPane = new JScrollPane(userAccountList);
                        userAccountList.addMouseListener(ualmAdapter);
                    }

                } catch (IOException ex) {
                    showMessage(ex.getMessage());
                }
            }
        });

        addProduct(availableCardList);

        addProduct(availableAccountList);

        uclmAdapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    String selectedProduct = userCardList.getSelectedValue();
                    for (Product p: userCardProducts) {
                        if(p.getProductName().equals(selectedProduct)){
                            lblProductName.setText(p.getProductName());
                            txtProductDescription.setText("Номер счёта: " + p.getAccId() + "\nБаланс: " + p.getBalance());
                            createChoosedProductLayout();
                        }
                    }
                }
            }
        };
        userCardList.addMouseListener(uclmAdapter);

        ualmAdapter = new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    String selectedProduct = userAccountList.getSelectedValue();
                    for (Product p: userAccountProducts) {
                        if(p.getProductName().equals(selectedProduct)){
                            lblProductName.setText(p.getProductName());
                            txtProductDescription.setText("Номер счёта: " + p.getAccId() + "\nБаланс: " + p.getBalance());
                            createChoosedProductLayout();
                        }
                    }
                }
            }
        };
        userAccountList.addMouseListener(ualmAdapter);
    }

    private List<String> initializeNames(ArrayList<Product> array, List<String> names){
        names = new ArrayList<>();
        int i = 1;
        for (Product p: array) {
            names.add(p.getProductName());
        }
        return names;
    }
    private JList<String> initializeNamesJList(List<String> names){
        return new JList<>(names.toArray(new String[0]));
    }
    private void addProduct(JList<String> availableProductList) {
        availableProductList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    String selectedProduct = availableProductList.getSelectedValue();
                    try {
                        client.getProduct(selectedProduct);
                    } catch (IOException ex) {
                        showMessage(ex.getMessage());
                    }
                }
            }
        });
    }
    public void chooseProduct(){
        if(choosedProduct != null){
            lblProductName.setText(choosedProduct.getProductName());
            txtProductDescription.setText(choosedProduct.getInfo());
            createChoosedProductLayout();
        }
    }
    public void showMessage(String msg) {JOptionPane.showMessageDialog(this, msg);
    }
    public void createProducts(ArrayList<Product> products){
        cardProducts = new ArrayList<>();
        accountProducts = new ArrayList<>();
        for (Product product:products
        ) {
            if(product.getType() == 1)
                cardProducts.add(product);
            else
                accountProducts.add(product);
        }
    }

    public void createUserProducts(ArrayList<Product> products){
        userProducts = products;
        userCardProducts = new ArrayList<>();
        userAccountProducts = new ArrayList<>();
        for (Product product:products
        ) {
            if(product.getType() == 1)
                userCardProducts.add(product);
            else
                userAccountProducts.add(product);
        }
    }
    public void setChoosedProduct(Product product){
        choosedProduct = product;
    }
    private void initializeGl(){
        getContentPane().removeAll();
        gl = new GroupLayout(getContentPane());
        setLayout(gl);

        gl.setAutoCreateGaps(true);
        gl.setAutoCreateContainerGaps(true);
    }
    private void createAuthLayout(){
        initializeGl();

        gl.setHorizontalGroup(
                gl.createSequentialGroup()
                        .addGap(8,8, Integer.MAX_VALUE)
                        .addGroup(gl.createParallelGroup()
                                .addGroup(
                                        gl.createSequentialGroup()
                                                .addComponent(lblPhone, MIN_SZ, MIN_SZ, MIN_SZ)
                                                .addGap(8)
                                                .addComponent(tfPhone,MAX_SZ, MAX_SZ, MAX_SZ)
                                )

                                .addGroup(
                                        gl.createSequentialGroup()
                                                .addComponent(lblPassword, MIN_SZ, MIN_SZ, MIN_SZ)
                                                .addGap(8)
                                                .addComponent(tfPassword,MAX_SZ, MAX_SZ, MAX_SZ)
                                )
                                .addGroup(
                                        gl.createSequentialGroup()
                                                .addGap(8,8,Integer.MAX_VALUE)
                                                .addComponent(btnAuth, MIN_SZ, MIN_SZ, MIN_SZ)
                                                .addGap(8)
                                                .addComponent(btnReg, MIN_SZ, MIN_SZ, MIN_SZ)
                                                .addGap(8,8,Integer.MAX_VALUE)
                                )
                        )
                        .addGap(8,8, Integer.MAX_VALUE)
        );
        gl.setVerticalGroup(
                gl.createSequentialGroup()
                        .addGap(8,8,Integer.MAX_VALUE)
                        .addGroup(
                                gl.createParallelGroup()
                                        .addComponent(lblPhone,MIN_SZ, MIN_SZ, MIN_SZ)
                                        .addComponent(tfPhone, MIN_SZ, MIN_SZ, MIN_SZ)
                        )
                        .addGap(4)

                        .addGroup(
                                gl.createParallelGroup()
                                        .addComponent(lblPassword,MIN_SZ, MIN_SZ, MIN_SZ)
                                        .addComponent(tfPassword, MIN_SZ, MIN_SZ, MIN_SZ)
                        )

                        .addGap(8)
                        .addGroup(
                                gl.createParallelGroup()
                                        .addComponent(btnReg,MIN_SZ, MIN_SZ, MIN_SZ)
                                        .addComponent(btnAuth, MIN_SZ, MIN_SZ, MIN_SZ)
                        )
                        .addGap(8,8,Integer.MAX_VALUE)
        );
        gl.linkSize(0, lblPhone, lblPassword);
        gl.linkSize(0, btnReg, btnAuth);

    }
    public void createAuthSuccessLayout() {
        initializeGl();

        gl.setHorizontalGroup(gl.createParallelGroup(GroupLayout.Alignment.CENTER)
                .addGroup(gl.createSequentialGroup()
                        .addComponent(lblName)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Integer.MAX_VALUE)
                        .addComponent(btnLogOut)
                )
                .addComponent(lblCard)
                .addGroup(gl.createSequentialGroup()
                        .addComponent(cardScrollPane)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCard)
                )
                .addComponent(lblAccount)
                .addGroup(gl.createSequentialGroup()
                        .addComponent(accountScrollPane)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnAccount)
                )
                .addComponent(btnTransfer)
        );

        gl.setVerticalGroup(gl.createSequentialGroup()
                .addGroup(gl.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(lblName)
                        .addComponent(btnLogOut)
                )
                .addGap(8)
                .addComponent(lblCard)
                .addGroup(gl.createParallelGroup()
                        .addComponent(cardScrollPane, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Integer.MAX_VALUE)
                        .addComponent(btnCard)
                )
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblAccount)
                .addGroup(gl.createParallelGroup()
                        .addComponent(accountScrollPane, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Integer.MAX_VALUE)
                        .addComponent(btnAccount)
                )
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnTransfer)
        );
    }


    private void createChooseTransferLayout(){
        initializeGl();

        gl.setHorizontalGroup(
                gl.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addGroup(gl.createSequentialGroup()
                                .addComponent(lblName)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Integer.MAX_VALUE)
                                .addComponent(btnLogOut)
                        )
                        .addComponent(btnSelfTransfer)
                        .addComponent(btnTransferToAnother)
                        .addComponent(btnCancel)
        );

        gl.setVerticalGroup(
                gl.createSequentialGroup()
                        .addGroup(gl.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(lblName)
                                .addComponent(btnLogOut)
                        )
                        .addGap(8, 8, Integer.MAX_VALUE)
                        .addComponent(btnSelfTransfer)
                        .addComponent(btnTransferToAnother)
                        .addComponent(btnCancel)
                        .addGap(8, 8, Integer.MAX_VALUE)
        );

        gl.linkSize(0, btnSelfTransfer, btnTransferToAnother);
    }

    private void createAddProductLayout(JScrollPane pane){
        initializeGl();

        gl.setHorizontalGroup(
                gl.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addGroup(gl.createSequentialGroup()
                                .addComponent(lblName)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Integer.MAX_VALUE)
                                .addComponent(btnLogOut)
                        )
                        .addComponent(pane)
                        .addComponent(btnCancel)

        );

        gl.setVerticalGroup(
                gl.createSequentialGroup()
                        .addGroup(gl.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(lblName)
                                .addComponent(btnLogOut)
                        )
                        .addGap(8, 8, Integer.MAX_VALUE)
                        .addComponent(pane)
                        .addGap(8, 8, Integer.MAX_VALUE)
                        .addComponent(btnCancel)
        );
    }

    private void createChoosedProductLayout(){
        initializeGl();

        gl.setHorizontalGroup(
                gl.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addGroup(gl.createSequentialGroup()
                                .addComponent(lblName)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Integer.MAX_VALUE)
                                .addComponent(btnLogOut)
                        )
                        .addComponent(lblProductName)
                        .addComponent(txtProductDescription)
                        .addGroup(gl.createSequentialGroup()
                            .addComponent(btnCancel)
                            .addComponent(btnAdd)
                        )

        );

        gl.setVerticalGroup(
                gl.createSequentialGroup()
                        .addGroup(gl.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(lblName)
                                .addComponent(btnLogOut)
                        )
                        .addGap(8, 8, Integer.MAX_VALUE)
                        .addComponent(lblProductName)
                        .addComponent(txtProductDescription)
                        .addGap(8, 8, Integer.MAX_VALUE)
                        .addGroup(gl.createParallelGroup()
                                .addComponent(btnCancel)
                                .addComponent(btnAdd)
                        )

        );
    }

    private void createTransferToAnotherLayout(){
        initializeGl();

        tfPhone.setText("");
        gl.setHorizontalGroup(
                gl.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addGroup(gl.createSequentialGroup()
                                .addComponent(lblName)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Integer.MAX_VALUE)
                                .addComponent(btnLogOut)
                        )
                        .addGroup(gl.createSequentialGroup()
                                .addGap(8, 8, Integer.MAX_VALUE)
                                .addGroup(gl.createParallelGroup()
                                        .addComponent(lblPhone)
                                        .addComponent(tfPhone)
                                        .addComponent(lblCard)
                                        .addComponent(cardScrollPane)
                                        .addComponent(lblSum)
                                        .addComponent(tfSum)
                                        .addGroup(gl.createSequentialGroup()
                                                .addGap(8, 8, Integer.MAX_VALUE)
                                                .addComponent(btnCancel)
                                                .addComponent(btnTransfer)
                                                .addGap(8, 8, Integer.MAX_VALUE)
                                        )
                                )
                                .addGap(8, 8, Integer.MAX_VALUE)
                        )


        );

        gl.setVerticalGroup(
                gl.createSequentialGroup()
                        .addGroup(gl.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(lblName)
                                .addComponent(btnLogOut)
                        )
                        .addGap(8, 8, Integer.MAX_VALUE)
                        .addComponent(lblPhone)
                        .addComponent(tfPhone, MIN_SZ, MIN_SZ, MIN_SZ)
                        .addComponent(lblCard)
                        .addComponent(cardScrollPane, MIN_SZ, MIN_SZ, MIN_SZ)
                        .addComponent(lblSum)
                        .addComponent(tfSum, MIN_SZ, MIN_SZ, MIN_SZ)
                        .addGroup(gl.createParallelGroup()
                                .addComponent(btnCancel)
                                .addComponent(btnTransfer)
                        )
                        .addGap(10, 10, Integer.MAX_VALUE)

        );

        btnTransfer.removeActionListener(transferActionListener);
        transferActionListener2 = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String phone = tfPhone.getText();
                String selectedCard = userCardList.getSelectedValue();
                String msg = "";

                Transfer t = new Transfer();
                t.setPhone1(user.getPhone());
                float sum;

                if(phone.equals("")){
                    msg += "Заполните поле для номера телефона.\n";
                }
                else {
                    t.setPhone2(phone);
                }
                if (!tfSum.getText().equals(""))
                    try {
                        sum = Float.parseFloat(tfSum.getText());
                        t.setSum(sum);
                    }
                    catch (NumberFormatException ex){
                        showMessage(ex.getMessage());
                    }
                else{
                    msg += "Введите сумму.\n";
                }
                boolean isCardSelected = false;
                for (Product p: userCardProducts) {
                    if(p.getProductName().equals(selectedCard))
                    {
                        t.setAccount1(p.getAccId());
                        isCardSelected = true;
                    }
                }
                if(!isCardSelected){
                    msg += "Выберите карту.\n";
                }
                if(!msg.equals("")){
                    showMessage(msg);
                }
                else{
                    try {
                        client.doTransfer(t);
                    } catch (IOException ex) {
                        showMessage(ex.getMessage());
                    }
                }
            }
        };
        btnTransfer.addActionListener(transferActionListener2);
    }

    private void createSelfTransferLayout(){
        initializeGl();

        gl.setHorizontalGroup(
                gl.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addGroup(gl.createSequentialGroup()
                                .addComponent(lblName)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Integer.MAX_VALUE)
                                .addComponent(btnLogOut)
                        )
                        .addGroup(gl.createSequentialGroup()
                                .addGap(8, 8, Integer.MAX_VALUE)
                                .addGroup(gl.createParallelGroup()
                                        .addComponent(lblTransferFrom)
                                        .addComponent(userProductPaneFrom)
                                        .addComponent(lblTransferTo)
                                        .addComponent(userProductPaneTo)
                                        .addComponent(lblSum)
                                        .addComponent(tfSum)
                                        .addGroup(gl.createSequentialGroup()
                                                .addGap(8, 8, Integer.MAX_VALUE)
                                                .addComponent(btnCancel)
                                                .addComponent(btnTransfer)
                                                .addGap(8, 8, Integer.MAX_VALUE)
                                        )
                                )
                                .addGap(8, 8, Integer.MAX_VALUE)
                        )


        );

        gl.setVerticalGroup(
                gl.createSequentialGroup()
                        .addGroup(gl.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(lblName)
                                .addComponent(btnLogOut)
                        )
                        .addGap(8, 8, Integer.MAX_VALUE)
                        .addComponent(lblTransferFrom)
                        .addComponent(userProductPaneFrom)
                        .addComponent(lblTransferTo)
                        .addComponent(userProductPaneTo)
                        .addComponent(lblSum)
                        .addComponent(tfSum, MIN_SZ, MIN_SZ, MIN_SZ)
                        .addGroup(gl.createParallelGroup()
                                .addComponent(btnCancel)
                                .addComponent(btnTransfer)
                        )
                        .addGap(10, 10, Integer.MAX_VALUE)

        );

        btnTransfer.removeActionListener(transferActionListener);
        transferActionListener3 = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String msg = "";
                String selectedProductFrom = userProductListFrom.getSelectedValue();
                String selectedProductTo = userProductListTo.getSelectedValue();

                Transfer t = new Transfer();
                float sum;

                t.setPhone1(user.getPhone());
                t.setPhone2(user.getPhone());

                boolean isProductFromSelected = false;
                for (Product p: userProducts) {
                    if(p.getProductName().equals(selectedProductFrom))
                    {
                        t.setAccount1(p.getAccId());
                        isProductFromSelected = true;
                    }
                }

                boolean isProductToSelected = false;
                for (Product p: userProducts) {
                    if(p.getProductName().equals(selectedProductTo))
                    {
                        t.setAccount2(p.getAccId());
                        isProductToSelected = true;
                    }
                }

                if(!isProductFromSelected) msg += "Выберите с какого счёта перевести.\n";
                if(!isProductToSelected) msg += "Выберите на какой счёт перевести.\n";
                if(isProductToSelected && isProductFromSelected && selectedProductFrom.equals(selectedProductTo)) msg += "Выбраны одинаковые счета.\n";

                if(!tfSum.getText().equals(""))
                    try {
                        sum = Float.parseFloat(tfSum.getText());
                        t.setSum(sum);
                    }
                    catch (NumberFormatException ex){
                        showMessage(ex.getMessage());
                    }
                else{
                    msg += "Введите сумму.\n";
                }

                if(!msg.equals("")){
                    showMessage(msg);
                }
                else{
                    try {
                        client.doTransfer(t);
                    } catch (IOException ex) {
                        showMessage(ex.getMessage());
                    }
                }
            }
        };
        btnTransfer.addActionListener(transferActionListener3);
    }


}
