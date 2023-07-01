import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import javax.swing.JComponent;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.metal.MetalTabbedPaneUI;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;
import org.jdesktop.animation.timing.interpolation.PropertySetter;

public class TableDesign extends JTabbedPane{
    public TableDesign() {
        setUI(new MaterialTabbedUI());
    }
    public class MaterialTabbedUI extends MetalTabbedPaneUI {

        public void setCurrentRectangle(Rectangle currentRectangle) {
            this.currentRectangle = currentRectangle;
            repaint();
        }

        private Animator animator;
        private Rectangle currentRectangle;
        private TimingTarget target;

        public MaterialTabbedUI() {
        }

        @Override
        public void installUI(JComponent jc) {
            super.installUI(jc);
            animator = new Animator(500);
            animator.setResolution(0);
            animator.setAcceleration(.5f);
            animator.setDeceleration(.5f);
            tabPane.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent ce) {
                    int selected = tabPane.getSelectedIndex();
                    if (selected != -1) {
                        if (currentRectangle != null) {
                            if (animator.isRunning()) {
                                animator.stop();
                            }
                            animator.removeTarget(target);
                            target = new PropertySetter(MaterialTabbedUI.this, "currentRectangle", currentRectangle, getTabBounds(selected, calcRect));
                            animator.addTarget(target);
                            animator.start();
                        }
                    }
                }
            });
        }

        @Override
        protected Insets getTabInsets(int i, int i1) {
            return new Insets(10, 10, 10, 10);
        }

        @Override
        protected void paintTabBorder(Graphics grphcs, int tabPlace, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
            Graphics2D select = (Graphics2D) grphcs.create();
            select.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            select.setColor(new Color(0, 0, 0));
            if (currentRectangle == null || !animator.isRunning()) {
                if (isSelected) {
                    currentRectangle = new Rectangle(x, y, w, h);
                }
            }
            if (currentRectangle != null) {
                if (tabPlace == TOP) {
                    select.fillRect(currentRectangle.x, currentRectangle.y + currentRectangle.height - 3, currentRectangle.width, 3);
                } else if (tabPlace == BOTTOM) {
                    select.fillRect(currentRectangle.x, currentRectangle.y, currentRectangle.width, 3);
                } else if (tabPlace == LEFT) {
                    select.fillRect(currentRectangle.x + currentRectangle.width - 3, currentRectangle.y, 3, currentRectangle.height);
                } else if (tabPlace == RIGHT) {
                    select.fillRect(currentRectangle.x, currentRectangle.y, 3, currentRectangle.height);
                }
            }
            select.dispose();
        }

        @Override
        protected void paintContentBorder(Graphics grphcs, int tabPlace, int selectedIndex) {
            Graphics2D select = (Graphics2D) grphcs.create();
            select.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            select.setColor(new Color(0, 0, 240));
            Insets insets = getTabAreaInsets(tabPlace);
            int width = tabPane.getWidth();
            int height = tabPane.getHeight();
            if (tabPlace == TOP) {
                int tabHeight = calculateTabAreaHeight(tabPlace, runCount, maxTabHeight);
                select.drawLine(insets.left, tabHeight, width - insets.right - 1, tabHeight);
            } else if (tabPlace == BOTTOM) {
                int tabHeight = height - calculateTabAreaHeight(tabPlace, runCount, maxTabHeight);
                select.drawLine(insets.left, tabHeight, width - insets.right - 1, tabHeight);
            } else if (tabPlace == LEFT) {
                int tabWidth = calculateTabAreaWidth(tabPlace, runCount, maxTabWidth);
                select.drawLine(tabWidth, insets.top, tabWidth, height - insets.bottom - 1);
            } else if (tabPlace == RIGHT) {
                int tabWidth = width - calculateTabAreaWidth(tabPlace, runCount, maxTabWidth) - 1;
                select.drawLine(tabWidth, insets.top, tabWidth, height - insets.bottom - 1);
            }
            select.dispose();
        }

        @Override
        protected void paintFocusIndicator(Graphics grphcs, int i, Rectangle[] rctngls, int i1, Rectangle rctngl, Rectangle rctngl1, boolean bln) {

        }

        @Override
        protected void paintTabBackground(Graphics g, int tabPlace, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
            if (tabPane.isOpaque()) {
                super.paintTabBackground(g, tabPlace, tabIndex, x, y, w, h, isSelected);
            }
        }
    }
}
