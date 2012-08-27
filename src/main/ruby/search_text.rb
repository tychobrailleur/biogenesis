require 'java'

search_field = javax.swing.JTextField.new(5)
search_field.setMaximumSize(search_field.getPreferredSize())
search_field.addKeyListener do |e|
  if e.getKeyCode() == java.awt.event.KeyEvent::VK_ENTER && search_field.getText() =~ /[0-9]+/
    organism = window.getWorld().findOrganismById(search_field.getText().to_i)
    window.getVisibleWorld().setSelectedOrganism(organism) if organism != nil
  end
end
window.getJMenuBar().add(search_field)
