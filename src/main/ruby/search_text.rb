require 'java'

java_import 'javax.swing.JTextField'
java_import 'java.awt.event.KeyEvent'

search_field = JTextField.new(5)
search_field.maximum_size = search_field.preferred_size
search_field.add_key_listener do |e|
  if e.key_code == KeyEvent::VK_ENTER && search_field.text =~ /[0-9]+/
    organism = window.world.find_organism_by_id(search_field.text.to_i)
    window.visible_world.selected_organism = organism if organism != nil
  end
end
window.jmenu_bar.add(search_field)
