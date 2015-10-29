Rails.application.routes.draw do

  # start at 'home/index' page
  root 'home#index'

  # home controller pages
  get 'home/index'

  # convention controller pages
  get 'convention/new'
  get 'convention/all'
  get 'convention/:name/index' => 'convention#index'
  get 'convention/:name/details' => 'convention#details'
  get 'convention/:name/events' => 'convention#events'
  get 'convention/:name/schedule' => 'convention#schedule'
  get 'convention/:convention_name/documents' => 'convention#documents'
  get 'convention/:convention_name/events/add' => 'convention#add_event'

  post 'convention/new' => 'convention#create_convention'
  post 'convention/:convention_name/rooms/add' => 'convention#add_room'
  post 'convention/:convention_name/hosts/add' => 'convention#add_host'
  post 'convention/:convention_name/events/add' => 'convention#create_event'
  post 'convention/:convention_name/documents/add' => 'convention#upload_document'

  # The priority is based upon order of creation: first created -> highest priority.
  # See how all your routes lay out with "rake routes".

  # You can have the root of your site routed with "root"
  # root 'welcome#index'

  # Example of regular route:
  #   get 'products/:id' => 'catalog#view'

  # Example of named route that can be invoked with purchase_url(id: product.id)
  #   get 'products/:id/purchase' => 'catalog#purchase', as: :purchase

  # Example resource route (maps HTTP verbs to controller actions automatically):
  #   resources :products

  # Example resource route with options:
  #   resources :products do
  #     member do
  #       get 'short'
  #       post 'toggle'
  #     end
  #
  #     collection do
  #       get 'sold'
  #     end
  #   end

  # Example resource route with sub-resources:
  #   resources :products do
  #     resources :comments, :sales
  #     resource :seller
  #   end

  # Example resource route with more complex sub-resources:
  #   resources :products do
  #     resources :comments
  #     resources :sales do
  #       get 'recent', on: :collection
  #     end
  #   end

  # Example resource route with concerns:
  #   concern :toggleable do
  #     post 'toggle'
  #   end
  #   resources :posts, concerns: :toggleable
  #   resources :photos, concerns: :toggleable

  # Example resource route within a namespace:
  #   namespace :admin do
  #     # Directs /admin/products/* to Admin::ProductsController
  #     # (app/controllers/admin/products_controller.rb)
  #     resources :products
  #   end
end
